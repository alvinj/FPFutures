package futures.v3_futureboard.controllers

import java.awt.Dimension
import javax.swing._

trait IFrameControllerInterface {

    def updateContent(): Unit

    protected val IFRAME_WIDTH = 480
    protected val IFRAME_HEIGHT = 240

    protected def iFrame: JInternalFrame
    protected val listModel = new DefaultListModel[String]
    protected val jList = new JList(listModel)
    protected val scrollpane = new JScrollPane(jList)
    protected var headlines = Seq[String]()

    scrollpane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS)
    scrollpane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS)

    def getFrame: JInternalFrame = {
        iFrame.setContentPane(scrollpane)
        //TODO figure out what "size" pieces are really needed
        jList.setSize(IFRAME_WIDTH, IFRAME_HEIGHT)
        jList.setMinimumSize(new Dimension(IFRAME_WIDTH, IFRAME_HEIGHT))
        scrollpane.setMinimumSize(new Dimension(jList.getWidth, jList.getHeight))
        iFrame.pack
        iFrame.setVisible(true)
        iFrame
    }


}
