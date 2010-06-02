/*
 * WorldCat_Bot_Servlet.groovy
 */
package com.appspot.worldcatbot;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.wave.api.AbstractRobot;
import com.google.wave.api.Blip;
import com.google.wave.api.event.DocumentChangedEvent;
import com.google.wave.api.event.WaveletSelfAddedEvent;

/**
 * This class provides the servlet that will answer requests over the wave robot
 * gateway
 * 
 * @author Keegan Witt
 * @version $date$ $rev$
 */
public class WorldCatBot extends AbstractRobot {
	private static final long serialVersionUID = 9146710414494280557L;
	private static final Logger LOG = Logger.getLogger(WorldCatBot.class.getName());

	@Override
	public String getRobotName() {
		return "worldcat-bot";
	}

	@Override
	public String getRobotAvatarUrl() {
		return "http://worldcat-bot.appspot.com/icon.gif";
	}

	@Override
	public String getRobotProfilePageUrl() {
		return "http://worldcat-bot.appspot.com/";
	}

	@Override
	public void onWaveletSelfAdded(WaveletSelfAddedEvent event) {
		event.getWavelet().reply("\nHi, I'm WorldCat-Bot."
			+ "\nType [wc:(author|title|isbn|issn|keyword|number|subject) {search terms}] to search WorldCat."
			+ "\nYou can only specify one field (author, title, etc). If you don't specify a field to search by, I'll search them all.");
	}

	@Capability(filter = "\\[.*\\]")
	@Override
	public void onDocumentChanged(DocumentChangedEvent event) {
		processBlip(event.getBlip());
	}

	/**
	 * Method to parse and replace text in blip
	 * 
	 * @param blip
	 *            the blip to parse
	 */
	private void processBlip(Blip blip) {
		String text = blip.getContent();

		// find all the search requests in the blip's text
		Pattern pattern = Pattern.compile("\\[wc(:all|:author|:title|:isbn|:issn|:keyword|:number|:subject)? [^\\]]+\\]");
		Matcher matcher = pattern.matcher(text);
		while (matcher.find()) {
			String match = matcher.group();
			String searchType = match.split("\\[wc:")[1].split(" ")[0];
			String tmp = match.split("\\[wc:\\w+\\s")[1];
			String query = tmp.substring(0, tmp.length() - 1);
			String url;

			if ("author".equalsIgnoreCase(searchType)) {
				url = "http://www.worldcat.org/search?q=au:" + query;
			} else if ("isbn".equalsIgnoreCase(searchType)) {
				url = "http://www.worldcat.org/search?q=bn:" + query;
			} else if ("issn".equalsIgnoreCase(searchType)) {
				url = "http://www.worldcat.org/search?q=n2:" + query;
			} else if ("keyword".equalsIgnoreCase(searchType)) {
				url = "http://www.worldcat.org/search?q=kw:" + query;
			} else if ("number".equalsIgnoreCase(searchType)) {
				url = "http://www.worldcat.org/search?q=no:" + query;
			} else if ("subject".equalsIgnoreCase(searchType)) {
				url = "http://www.worldcat.org/search?q=su:" + query;
			} else if ("title".equalsIgnoreCase(searchType)) {
				url = "http://www.worldcat.org/search?q=ti:" + query;
			} else if ("all".equalsIgnoreCase(searchType)) {
				url = "http://www.worldcat.org/search?qt=worldcat_org_all&q=" + query;
			} else {
				url = "http://www.worldcat.org/search?qt=worldcat_org_all&q=" + query;
			}

			if (LOG.isLoggable(Level.INFO)) {
			    LOG.info("Added hyperlink to " + match + ".");
			}
			blip.first(match).annotate("link/manual", url);
		}
	}

}
