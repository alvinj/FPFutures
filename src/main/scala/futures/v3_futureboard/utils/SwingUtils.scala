package futures.v3_futureboard.utils

import javax.swing.SwingUtilities

object SwingUtils {

    def invokeLater(callback: => Unit) {
        SwingUtilities.invokeLater(() => {
            callback
        })
    }

}


