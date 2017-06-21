package demo

import demo.pages.HomePage

/**
 * Simple test for the home page.
 */
class HomePageSpec extends BaseGebSpec {
    def "adding a product"() {
        given:
        HomePage initialPage = to(HomePage)
        assert initialPage.productItems.size == 0

        when:
        HomePage addSuccessPage = initialPage.addNewProduct()

        then:
        assert addSuccessPage.productItems.size == 1
    }
}
