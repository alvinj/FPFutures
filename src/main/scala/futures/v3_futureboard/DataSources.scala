package futures.v3_futureboard

trait NewsSourceInterface {
    def title: String
}


/**
  * a news source, like the Chicago Tribune or Denver Post
  */
case class NewsSource (
    title: String,
    url: String,
    rootNodeToLookFor: String,
    attributeByName: String,
    whatToLookForInsideAttribute: String,
    phrasesOfInterest: Seq[String]
) extends NewsSourceInterface


/**
  * what i need for Twitter
  */
case class TwitterSource (
     title: String,
     url: String
) extends NewsSourceInterface


