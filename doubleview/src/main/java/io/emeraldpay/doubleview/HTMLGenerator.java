package io.emeraldpay.doubleview;

/**
 * Generates the final HTML page, with the rendered React component and the client-side code to initialize it.
 * It's supplied with the rendered parts, and other details needed to display the page in the browser, and it supposed to wrap it into a final HTML
 *
 */
public interface HTMLGenerator {

    /**
     * Set a value to be included in the head (i.e., in the `&lt;head&gt;&lt;/head&gt;` block) of the HTML page.
     * @param head
     */
    void addHead(String head);

    /**
     * Set the rendered React as body of the HTML page.
     * @param body
     */
    void setViewBody(String body);

    /**
     * Set a value to be included after the rendered part (i.e., before the closing `&lt;/body&gt;` tag) of the HTML page.
     * This value includes data required to initialize the React component on the client side.
     *
     * @param postBody
     */
    void addPostBody(String postBody);

    /**
     * Generate the final HTML page.
     * @return
     */
    String generate();

}
