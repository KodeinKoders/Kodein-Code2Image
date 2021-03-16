import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.HTMLElement
import kotlin.js.Promise

@JsNonModule
@JsModule("highlight.js")
external val hljs: HljsAPI

external interface HljsAPI {
    fun highlightBlock(element: HTMLElement)
    fun listLanguages(): Array<String>
}

@JsNonModule
@JsModule("html2canvas")
external fun html2canvas(element: HTMLElement, options: dynamic = definedExternally): Promise<HTMLCanvasElement>
