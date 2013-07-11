/*
 * ﻿Copyright (C) 2013 Atlas of Living Australia
 * All Rights Reserved.
 *
 * The contents of this file are subject to the Mozilla Public
 * License Version 1.1 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 */

package au.org.ala.bhlftindex

import groovy.xml.MarkupBuilder

import java.text.SimpleDateFormat

/**
 *
 */
class BHLTagLib {

    static namespace = 'bhl'

    def groovyPageLocator

    /**
     * @attr active
     */
    def navbar = { attrs ->

        def mb = new MarkupBuilder(out)

        mb.ul(class:'nav') {
            li(class:attrs.active == 'home' ? 'active' : '') {
                    a(href:createLink(uri: '/')) {
                        i(class:"icon-home") {
                            mkp.yieldUnescaped("&nbsp;")
                        }
                        mkp.yieldUnescaped("&nbsp")
                        mkp.yield(message(code:'default.home.label', default: 'Home'))}
            }
            li(class:attrs.active == 'admin' ? 'active' : '') {
                a(href:createLink(controller: 'admin')) {
                    i(class:"icon-wrench") {
                        mkp.yieldUnescaped("&nbsp;")
                    }
                    mkp.yieldUnescaped("&nbsp")
                    mkp.yield(message(code:'default.about.label', default: 'Administration'))
                }
            }
        }

    }

    /**
     * @attr date
     */
    def formatDateStr =  { attrs, body ->

        Date theDate = null

        if (attrs.date) {
            if (attrs.date instanceof String) {
                theDate = DateUtils.tryParse(attrs.date)
            } else if (attrs.date instanceof Date) {
                theDate = attrs.date
            }
            if (theDate) {
                SimpleDateFormat sdf = null
                if (params.dateFormat) {
                    sdf = new SimpleDateFormat(params.dateFormat)
                } else {
                    sdf = new SimpleDateFormat("dd/MM/yyyy")
                }
                out << sdf.format(theDate)
                return
            }
        }
    }

    /**
     * @attr active
     * @attr title
     * @attr href
     */
    def breadcrumbItem = { attrs, body ->
        def active = attrs.active
        if (!active) {
            active = attrs.title
        }
        def current = pageProperty(name:'page.pageTitle')?.toString()

        def mb = new MarkupBuilder(out)
        mb.li(class: active == current ? 'active' : '') {
            a(href:attrs.href) {
                i(class:'icon-chevron-right') { mkp.yieldUnescaped('&nbsp;')}
                mkp.yield(attrs.title)
            }
        }
    }

    def spinner = { attrs, body ->
        def mb = new MarkupBuilder(out)
        mb.img(src: resource(dir:'/images', file:'spinner.gif')) {

        }
    }

    def navSeperator = { attrs, body ->
        out << "&nbsp;&#187;&nbsp;"
    }

    def loading = { attrs, body ->
        def message = attrs.message ?: "Loading..."
        def mb = new MarkupBuilder(out)
        mb.span() {
            mb.img(src:resource(dir: '/images', file:'spinner.gif'))
            mkp.yieldUnescaped("&nbsp;")
            mkp.yield(message)
        }
    }

    def homeBreadCrumb = { attrs, body ->
        // <a href="${createLink(controller: 'map', action: 'index')}">Map</a>
        def mb = new MarkupBuilder(out)
        mb.span(class:'sts-breadcrumb') {
            a(href:createLink(controller: 'map', action: 'index')) {
                mkp.yield("Main Map")
            }
        }
    }

}
