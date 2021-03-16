import react.dom.render
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.css.*
import react.child
import styled.injectGlobal

fun main() {
    injectGlobal {
        universal {
            margin(0.px)
            padding(0.px)
        }
        "html, body, #root" {
            width = 100.pct
            height = 100.pct
            fontFamily = "Picon"
        }
    }

    window.onload = {
        render(document.getElementById("root")) {
            child(App)
        }
    }
}
