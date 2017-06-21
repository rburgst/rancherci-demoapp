package demo.pages

import geb.Module
import geb.Page

/**
 * Created by rainer on 19.05.2017.
 */
class HomePage extends Page {
    static url = '/'

    static at = { title == 'Product list' }

    static content = {
        productItems {
            $("ul.list-group li").moduleList(ProductRow)
        }

        addProductButton(to: HomePage) { $("#add-button") }
    }

    HomePage addNewProduct() {
        addProductButton.click()

        browser.page
    }
}

class ProductRow extends Module {
    static content = {
        cell { $("li", it) }
        productName { cell(0).text() }
    }
}