import kotlinext.js.jsObject
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.css.*
import kotlinx.dom.clear
import kotlinx.html.InputType
import kotlinx.html.classes
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onClickFunction
import kotlinx.html.unsafe
import org.w3c.dom.*
import org.w3c.dom.events.EventListener
import org.w3c.dom.events.KeyboardEvent
import org.w3c.dom.events.MouseEvent
import react.*
import react.dom.*
import styled.*


val App = functionalComponent<RProps>("App") {

    var fileName by useState("code.kt")
    var content by useState("")
    var lang by useState("kotlin")

    val codeRef = useRef<HTMLElement?>(null)
    val renderRef = useRef<HTMLDivElement?>(null)

    useEffect(listOf(content, lang)) {
        val code = codeRef.current!!
        code.clear()
        code.textContent = content.trimIndent().trim()
        hljs.highlightBlock(codeRef.current!!)
    }

    fun download() {
        html2canvas(
            renderRef.current!!,
            jsObject {
                backgroundColor = null
            }
        ).then { canvas ->
            val anchor = document.createElement("a") as HTMLAnchorElement
            val name = fileName.takeIf { it.isNotEmpty() } ?: "code"
            anchor.download = "$fileName.png"
            anchor.href = canvas.toDataURL("image/png;base64")
            anchor.dispatchEvent(MouseEvent("click"))
        }
    }

    useEffectWithCleanup {
        val listener = EventListener {
            it as KeyboardEvent
            if (it.key == "s" && (it.metaKey || it.ctrlKey)) {
                it.preventDefault()
                download()
            }
        }
        window.addEventListener("keydown", listener)
        ({ window.removeEventListener("keydown", listener) })
    }

    styledDiv {
        css {
            display = Display.flex
            flexDirection = FlexDirection.column
            minHeight = 100.pct
        }

        styledDiv {
            css {
                padding(0.8.rem)
                display = Display.flex
                flexDirection = FlexDirection.row
                justifyContent = JustifyContent.center
                alignItems = Align.center
            }

            styledButton {
                css {
                    padding(vertical = 0.2.rem)
                    fontFamily = "Picon"
                    fontSize = 1.rem
                    width = 15.rem
                    margin(horizontal = 2.rem)
                }
                attrs.onClickFunction = { download() }
                b { +"Generate!" }
                +" (ctrl+s | cmd+s)"
            }

            styledSelect {
                css {
                    fontFamily = "Picon"
                    fontSize = 1.2.rem
                    width = 15.rem
                    margin(horizontal = 2.rem)
                }
                attrs.value = lang
                hljs.listLanguages().sorted().forEach {
                    option { +it }
                }
                attrs.onChangeFunction = {
                    lang = it.target.unsafeCast<HTMLSelectElement>().value
                }
            }
        }

        styledDiv {
            css {
                flexGrow = 1.0
                display = Display.flex
                flexDirection = FlexDirection.row
                width = 100.pct
            }

            styledDiv {
                css {
                    display = Display.flex
                    flexDirection = FlexDirection.column
                    flexGrow = 1.0
                    flexBasis = FlexBasis.zero
                    padding(0.8.rem)
                }

                styledInput(InputType.text) {
                    attrs.value = fileName
                    css {
                        borderStyle = BorderStyle.none
                        fontFamily = "Picon"
                        fontSize = 1.1.rem
                        fontWeight = FontWeight.w500
                        whiteSpace = WhiteSpace.nowrap
                        outline = Outline.none
                        marginBottom = 0.5.rem
                        color = Color("#575757")
                        backgroundColor = Color("#F8F8F8")
                    }
                    attrs.onChangeFunction = {
                        fileName = it.target.unsafeCast<HTMLInputElement>().value
                    }
                }

                styledTextArea {
                    attrs.value = content
                    attrs.placeholder = "Code here..."
                    css {
                        flexGrow = 1.0
                        resize = Resize.none
                        borderStyle = BorderStyle.none
                        fontFamily = "JetBrains Mono"
                        fontSize = 1.0.rem
                        whiteSpace = WhiteSpace.nowrap
                        outline = Outline.none
                        backgroundColor = Color("#F8F8F8")
                    }
                    attrs.onChangeFunction = {
                        val textarea = it.target as HTMLTextAreaElement
                        textarea.style.height = "1px"
                        textarea.style.height = "${textarea.scrollHeight + 25}px"
                        content = textarea.value
                    }
                }
            }

            styledDiv {
                css {
                    flexGrow = 1.0
                    flexBasis = FlexBasis.zero
                    display = Display.flex
                    justifyContent = JustifyContent.flexStart
                    alignItems = Align.flexStart
                    backgroundColor = Color("#480F40")
                    color = Color.white
                    overflow = Overflow.auto
                    padding(0.8.rem)
                    fontSize = 1.0.rem
                }

                styledDiv {
                    ref = renderRef
                    css {
                        display = Display.flex
                        flexDirection = FlexDirection.column
                    }
                    styledP {
                        css {
                            marginBottom = 0.5.rem
                            fontFamily = "Picon"
                            fontSize = 1.1.rem
                            fontWeight = FontWeight.w500
                        }
                        attrs.classes += "hljs-comment"
                        if (fileName.isNotEmpty()) +fileName
                        else attrs.unsafe { +"&nbsp;" }
                    }
                    styledDiv {
                        css {
                            display = Display.flex
                            flexDirection = FlexDirection.row
                            universal {
                                fontFamily = "JetBrains Mono"
                            }
                        }
                        styledPre {
                            css {
                                textAlign = TextAlign.right
                                paddingRight = 1.rem
                            }
                            attrs.classes += "hljs-comment"
                            content.trim().lines().indices.forEach {
                                +"${it + 1}\n"
                            }
                        }
                        styledPre {
                            css {
                                paddingRight = 0.5.em
                            }
                            code(lang) {
                                ref = codeRef
                            }
                        }
                    }
                }

            }
        }

    }
}
