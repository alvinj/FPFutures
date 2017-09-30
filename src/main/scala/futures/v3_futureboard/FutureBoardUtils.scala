package futures.v3_futureboard

import java.awt.Dimension
import java.awt.event.{ActionEvent, ActionListener, WindowAdapter, WindowEvent}
import java.util.Date
import javax.swing._

import futures.v3_futureboard.controllers.{IFrameControllerInterface, NewsSourceIFrameController}

object FutureBoardUtils {

    def configMainFrameSizeAndLocation(f: JFrame) = {
        f.setSize(new Dimension(800, 600))
        f.setLocationRelativeTo(null)
    }

    def configExitMenuItem(exitMenuItem: JMenuItem): Unit = {
        exitMenuItem.addActionListener(new ActionListener() {
            def actionPerformed(e: ActionEvent) {
                System.exit(0)
            }
        })
    }

    /**
      * currently this function takes the controllers as an input
      * parameter because the “update” menu item calls the controllers
      * to update their content.
      */
    def configureMenu(
        jFrame: JFrame,
        menuBar: JMenuBar,
        fileMenu: JMenu,
        exitMenuItem: JMenuItem,
        updateMenuItem: JMenuItem,
        updateController: UpdateControllerInterface
    ): Unit = {
        menuBar.add(fileMenu)
        jFrame.setJMenuBar(menuBar)
        configUpdateMenuItem(updateMenuItem, updateController)
        FutureBoardUtils.configExitMenuItem(exitMenuItem)
        fileMenu.add(updateMenuItem)
        fileMenu.add(exitMenuItem)
    }

    private def configUpdateMenuItem(
        updateMenuItem: JMenuItem,
        updateController: UpdateControllerInterface
    ): Unit = {
        updateMenuItem.addActionListener(new ActionListener() {
            def actionPerformed(e: ActionEvent) {
                updateController.updateContent()
            }
        })
    }

    /**
      * this function uses `SwingUtilities.invokeLater`, so it does not
      * have to be called on the Swing EDT.
      */
    def updateIFrameContents(newsControllers: Seq[NewsSourceIFrameController]): Unit = {
        import javax.swing.SwingUtilities
        SwingUtilities.invokeLater(() => {
            for (c <- newsControllers) c.updateContent()
        })
    }

    def makeMainFrameVisible(jFrame: JFrame): Unit = {
        import javax.swing.SwingUtilities
        SwingUtilities.invokeLater(new Runnable() {
            override def run(): Unit = {
                jFrame.setVisible(true)
                jFrame.addWindowListener(new WindowAdapter() {
                    override def windowClosing(e: WindowEvent) {
                        jFrame.setVisible(false)
                        jFrame.dispose()
                        System.exit(0)
                    }
                })
            }
        })
    }

    /**
      * TODO accept the controllers as a varargs parameter
      * (controllers: IFrameControllerInterface*).
      * TODO automatically lay out the size and position
      * of the internal frames.
      */
    def addIFramesToDesktopPane(
        tribuneController: IFrameControllerInterface,
        postController: IFrameControllerInterface,
        twitterController: IFrameControllerInterface,
        desktopPane: JDesktopPane
    ): Unit = {
        val tribuneFrame = tribuneController.getFrame
        tribuneFrame.setLocation(50, 20)
        desktopPane.add(tribuneFrame)

        val denverPostFrame = postController.getFrame
        denverPostFrame.setLocation(400, 20)
        desktopPane.add(denverPostFrame)

        val twitterFrame = twitterController.getFrame
        twitterFrame.setLocation(50, 240)
        twitterFrame.setSize(new Dimension(650, 300))
        desktopPane.add(twitterFrame)
    }




}
