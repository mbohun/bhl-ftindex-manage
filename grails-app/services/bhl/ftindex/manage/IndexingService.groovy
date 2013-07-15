package bhl.ftindex.manage

import org.apache.commons.lang.StringUtils
import org.apache.solr.client.solrj.SolrServer
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer
import org.apache.solr.client.solrj.impl.HttpSolrServer
import org.apache.solr.common.SolrInputDocument
import org.apache.solr.core.CoreContainer

import java.util.concurrent.atomic.AtomicInteger
import java.util.regex.Matcher
import java.util.regex.Pattern

class IndexingService {

    def grailsApplication
    def BHLService

    private SolrServer _solrServer;
   	private CoreContainer _coreContainer;
   	private final byte[] _serverLock = [];

    private static final Pattern SINGLE_YEAR_PATTERN = Pattern.compile("(\\d{4})");
    private static final Pattern YEAR_RANGE_PATTERN = Pattern.compile("(\\d{4})\\s*[^\\d]\\s*(\\d{4})");
    private static final Pattern ABBREV_RANGE_PATTERN = Pattern.compile("(\\d{4})\\s*[^\\d]\\s*(\\d{2})");

    def indexItem(Item item) {

        println "Indexing Item ${item.itemId}"

        final SolrServer server = createSolrServer();
        try {
            final AtomicInteger pageCount = new AtomicInteger(0);
            def itemJSON = this.BHLService.getItemMetaData(item.itemId, true, true)
            def titleData = this.BHLService.getTitleMetaData(item, false)

            itemJSON?.Result?.Pages?.each { page ->
                indexPage(item, page.PageID?.toString(), page.OcrText, server, itemJSON.Result, titleData?.Result)
                pageCount.incrementAndGet()

                if (pageCount.get() % 100 == 0) {
                    try {
                        server.commit();
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }

            }

            if (pageCount.get() > 0) {
                server.commit();
            }

            println "${pageCount.get()} pages indexed for item ${item.itemId}"

        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex
        }
    }

    private static void indexPage(Item item, String pageId, String pageText, SolrServer server, itemMetaData, titleMetaData) {
   		if (!StringUtils.isEmpty(pageText)) {
   			SolrInputDocument doc = new SolrInputDocument();
   			doc.addField("id", pageId, 1.0f);
   			doc.addField("name", item.title, 1.0f);
   			doc.addField("text", pageText);
   			doc.addField("internetArchiveId", item.internetArchiveId);
   			doc.addField("itemId", item.itemId);
   			doc.addField("pageId", pageId, 1.0f);
   			doc.addField("pageUrl", String.format("http://biodiversitylibrary.org/pageimage/%s", pageId));

   			if (itemMetaData != null) {
   				addItemMetadata(doc, itemMetaData);
   				if (titleMetaData) {
   					addTitleMetadata(doc, titleMetaData);
   				}
   			}

   			try {
   				server.add(doc);
   			} catch (Exception ex) {
   				throw new RuntimeException(ex);
   			}
   		}
   	}

   	private static void addItemMetadata(SolrInputDocument doc, metadata) {
   		String year = metadata.Year
   		if (!StringUtils.isEmpty(year)) {
   			YearRange range = parseYearRange(year);
   			if (range != null) {
   				doc.addField("startYear", range.startYear);
   				doc.addField("endYear", range.endYear);
   			}
   		}

   		addField(metadata, "Volume", "volume", doc);
   		addField(metadata, "Contributor", "contributor", doc);
   		addField(metadata, "Source", "source", doc);

   		int titleId = metadata.PrimaryTitleID as Integer
   		doc.addField("titleId", titleId);

   	}

   	private static void addTitleMetadata(SolrInputDocument doc, titleData) {
   		// Full title
   		addField(titleData, "FullTitle", "fullTitle", doc);
   		// Publisher Name
   		addField(titleData, "PublisherName", "publisherName", doc);
   		// Publisher Place
   		addField(titleData, "PublisherPlace", "publisherPlace", doc);

   		// Author(s)
   		for (String author : selectField(titleData["Authors"], "Name")) {
   			doc.addField("author", author);
   		}

   		// Author ID
   		for (String authorId : selectField(titleData["Authors"], "CreatorID")) {
   			doc.addField("authorId", authorId);
   		}

   		// Subject(s)
   		for (String subject : selectField(titleData["Subjects"], "SubjectText")) {
   			doc.addField("subject", subject);
   		}

   		// Publication Dates
   		String date = titleData.PublicationDate
   		YearRange range = parseYearRange(date);
   		if (range != null) {
   			doc.addField("publicationStartYear", range.startYear);
   			doc.addField("publicationEndYear", range.startYear);
   		}

   	}

   	private static void addField(obj, String jsonfield, String indexField, SolrInputDocument doc) {
   		String value = obj[jsonfield]
   		if (!StringUtils.isEmpty(value)) {
   			doc.addField(indexField, value);
   		}
   	}

   	private static List<String> selectField(parent, String textField) {
   		ArrayList<String> results = new ArrayList<String>();
   		parent?.each {
            String name = it[textField]
            if (name) {
                results.add(name);
            }
        }

   		return results;
   	}

   	public static YearRange parseYearRange(String range) {

   		if (StringUtils.isEmpty(range)) {
   			return null;
   		}

   		if (StringUtils.isNumeric(range)) {
   			return new YearRange(range, range);
   		}

   		// Look for YYYY-YYYY
   		Matcher m = YEAR_RANGE_PATTERN.matcher(range);
   		if (m.find()) {
   			return new YearRange(m.group(1), m.group(2));
   		}

   		// Look for YYYY-YY
   		m = ABBREV_RANGE_PATTERN.matcher(range);
   		if (m.find()) {
   			String start = m.group(1);
   			return new YearRange(start, start.substring(0, 2) + m.group(2));
   		}

   		// Look for any for 4 consecutive digits!
   		m = SINGLE_YEAR_PATTERN.matcher(range);
   		if (m.find()) {
   			return new YearRange(m.group(1), m.group(1));
   		}

   		return null;
   	}

   	public static class YearRange {

   		public YearRange() {
   		}

   		public YearRange(int start, int end) {
   			startYear = start;
   			endYear = end;
   		}

   		public YearRange(String start, String end) {
   			startYear = Integer.parseInt(start);
   			endYear = Integer.parseInt(end);
   		}

   		public int startYear;
   		public int endYear;
   	}


    /**
   	 * Create a new instance of the Solr server API facade
   	 *
   	 * @return
   	 */
   	private SolrServer createSolrServer() {

        def serverUrl =  grailsApplication.config.ala.bhlidx.solr.server

   		try {
   			synchronized (_serverLock) {
   				if (_solrServer == null) {
   					if (!StringUtils.isEmpty(serverUrl)) {
   						if (serverUrl.startsWith("http://")) {
   							_solrServer = new HttpSolrServer(serverUrl);
   						} else {
   						  // Note that the following property could be set through JVM level arguments too
   						  System.setProperty("solr.solr.home", serverUrl);
   						  CoreContainer.Initializer initializer = new CoreContainer.Initializer();
   						  _coreContainer = initializer.initialize();
   						  _solrServer = new EmbeddedSolrServer(_coreContainer, "");
   						}
   					} else {
   						throw new RuntimeException("Neither a local SOLR path or a SOLR HTTP Url was specified!");
   					}
   				}
   			}

   			return _solrServer;
   		} catch (Exception ex) {
   			throw new RuntimeException(ex);
   		}
   	}

}
