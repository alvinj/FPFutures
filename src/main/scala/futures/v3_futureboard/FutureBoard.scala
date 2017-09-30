package futures.v3_futureboard

import javax.swing._

import futures.v3_futureboard.controllers.{IFrameControllerInterface, NewsSourceIFrameController, TwitterSourceIFrameController}

trait UpdateControllerInterface {
    def updateContent(): Unit
}

object FutureBoard extends App with UpdateControllerInterface {

    // swing things
    val jFrame = new JFrame("Future Board")
    val desktopPane = new JDesktopPane()
    val menuBar = new JMenuBar()
    val fileMenu = new JMenu("File")
    val updateMenuItem = new JMenuItem("Update")
    val exitMenuItem = new JMenuItem("Exit")

    val tribuneNewsSource = NewsSource(
        "Chicago Tribune",
        "http://www.chicagotribune.com/sports/",
        "h3",
        "class",
        "trb_outfit_relatedListTitle",
        List("Cubs", "Bears", "Rizzo", "Bryant", "Arrieta", "Lester", "Glennon", "Schwarber")
    )

    val denverPostNewsSource = NewsSource(
        "Denver Post",
        "http://www.denverpost.com/sports/denver-broncos/",
        "a",
        "class",
        "article-title",
        List(
            "Broncos", "Siemian", "Harris", "Manning", "Miller", "DeMarcus",
            "Osweiler", "Anderson", "Lynch"
        )
    )

    val twitterSource = TwitterSource(
        "Twitter #scala",
        "https://twitter.com/search?q=%23scala&src=typd"
    )


    // MVC controllers
    val tribuneController = new NewsSourceIFrameController(tribuneNewsSource)
    val postController = new NewsSourceIFrameController(denverPostNewsSource)
    val twitterController = new TwitterSourceIFrameController(twitterSource)
    val iFrameControllers: Seq[IFrameControllerInterface] = Seq(
        tribuneController, postController, twitterController
    )


    // “main” begins
    FutureBoardUtils.configMainFrameSizeAndLocation(jFrame)

    //TODO don’t pass the controllers here; just use a callback to us
    FutureBoardUtils.configureMenu(
        jFrame,
        menuBar,
        fileMenu,
        exitMenuItem,
        updateMenuItem,
        this
    )
    jFrame.setContentPane(desktopPane)

    FutureBoardUtils.addIFramesToDesktopPane(
        tribuneController,
        postController,
        twitterController,
        desktopPane
    )

    FutureBoardUtils.makeMainFrameVisible(jFrame)

    /**
      * Enable this if desired. This will cause the application
      * to retrieve its results from the web pages as soon as
      * the application is started.
      */
    //updateContent()


    /**
      * when called, tell all of the controllers to update their content
      */
    def updateContent(): Unit = {
        for (c <- iFrameControllers) c.updateContent()
    }

}







