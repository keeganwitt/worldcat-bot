/*
 * WorldCat_Bot_Servlet.groovy
 */

package com.appspot.worldcatbot

import com.google.wave.api.*

/**
 * This class provides the servlet that will answer requests over the wave
 * robot gateway
 * 
 * @author Keegan Witt
 */
public class WorldCat_Bot_Servlet extends AbstractRobotServlet {
    @Override
    public void processEvents(RobotMessageBundle bundle) {
        Wavelet wavelet = bundle.getWavelet()
        
        // if just added, send greeting
        if (bundle.wasSelfAdded()) {
            Blip blip = wavelet.appendBlip()
            TextView textView = blip.getDocument()
            textView.append("Hi, I'm WorldCat-Bot." + 
                    "\nType <wc:(author|title|isbn|issn|keyword|number|subject) {your search goes here}> to search WorldCat." +
                    "\nYou can only specify one field (author, title, etc). If you don't specify a field to search by, I'll search them all.")
        }
        
        // go through all the blips not submitted by WorldCat-Bot and parse them
        for (Event event : bundle.getEvents()) {
            if (event.getType() == EventType.BLIP_SUBMITTED
            && !(event.getModifiedBy()
            .equalsIgnoreCase("worldcat-bot@appspot.com"))) {
                parseBlip(event.getBlip())
            }
        }
    }
    
    /**
     * Method to parse and replace text in blip
     * @param blip the blip to parse
     */
    private parseBlip(Blip blip) {
        String text = blip.getDocument().getText()
        TextView textView = blip.getDocument()
        
        // find all the search requests in the blip's text
        def searches = text.findAll(/<wc(:all|:author|:title|:isbn|:issn|:keyword|:number|:subject)? [^>]+>/)
        if (!searches) {            
            return // skip to end if text doesn't contain a request
        }
        
        // find the search type for each request
        def types = []
        searches.eachWithIndex() {String search, int i ->
            if (search.startsWith("<wc:")) {
                types[i] = search.split("<wc:")[1].find(/\w+/)
            } else { // no type specified, will use default type, which searches all
            }
        }
        
        // build the urls for each reqeust, based on it's search type
        def urls = []
        searches.eachWithIndex() {String search, int i ->
            String query = search.replaceFirst("<wc(:all|:author|:title|:isbn|:issn|:keyword|:number|:subject)? ", "").replace(">", "").replace(" ", "+") + ''
            switch(types[i]) {
                case "author":
                    urls[i] = "http://www.worldcat.org/search?q=au:" + query
                    break
                case "isbn":
                    urls[i] = "http://www.worldcat.org/search?q=bn:" + query
                    break
                case "issn":
                    urls[i] = "http://www.worldcat.org/search?q=n2:" + query
                    break
                case "keyword":
                    urls[i] = "http://www.worldcat.org/search?q=kw:" + query
                    break
                case "number":
                    urls[i] = "http://www.worldcat.org/search?q=no:" + query
                case "subject":
                    urls[i] = "http://www.worldcat.org/search?q=su:" + query
                    break
                case "title":
                    urls[i] = "http://www.worldcat.org/search?q=ti:" + query
                    break
                case "all":
                default:
                    urls[i] = "http://www.worldcat.org/search?qt=worldcat_org_all&q=" + query
                    break
            }
        }
        
        // replace request text with url(s) in blip
        searches.eachWithIndex() {String search, int i ->
            int place = text.indexOf(search)
            textView.setAnnotation(new Range(place, place + search.length()), "link/manual", urls[i]) 
        }
    }
}
