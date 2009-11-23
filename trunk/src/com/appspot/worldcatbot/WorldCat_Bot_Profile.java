/*
 * WorldCat_Bot_Profile.Java
 */

package com.appspot.worldcatbot;

import com.google.wave.api.ProfileServlet;

/**
 * This class provides the profile for WorldCat-Bot
 * 
 * @author Keegan Witt
 */
public class WorldCat_Bot_Profile extends ProfileServlet {

	private static final long serialVersionUID = 2771052784344813056L;

	@Override
	public String getRobotName() {
		return "WorldCat-Bot";
	}

	@Override
	public String getRobotAvatarUrl() {
		return "http://worldcat-bot.appspot.com/icon.gif";
	}

	@Override
	public String getRobotProfilePageUrl() {
		return "http://worldcat-bot.appspot.com/";
	}
}