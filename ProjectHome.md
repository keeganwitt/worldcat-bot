## About ##
WorldCat-Bot adds search links to [WorldCat](http://www.worldcat.org/) inside Google Wave. It is an unofficial project, not currently affiliated with WorldCat, OCLC, or any of its affiliates. It is hosted on the Google App Engine [here](http://worldcat-bot.appspot.com)

## Usage ##
First, add WorldCat-Bot to your wave by adding the address _worldcat-bot@appspot.com_. Then add a search anywhere in your text, it will look like this<br />
_<wc:(author|title|isbn|issn|keyword|number|subject) {your search goes here}>_<br />
You have to pick one of the fields to search on (author, title, etc). Otherwise, It will search on all fields. Here's an example<br />
_<wc:author arthur conan doyle}>_<br />
It will return a link to that search on WorldCat, like this<br />
_http://www.worldcat.org/search?q=au:arthur+conan+doyle_

## FAQ ##
**The result isn't hyperlinked!**<br />
> This is a problem with Wave. Usually you can click the link even if it isn't blue and underlined (I know it works for Firefox and Chrome). If not, try clicking edit then done on the blip to re-submit.<br />
**It's soooo slooow!**<br />
> Yea, this sometimes happens with Wave. It's still a preview release. Hopefully, Google will address this soon.