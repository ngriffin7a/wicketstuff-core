package org.wicketstuff.openlayers3.api.overlay;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.string.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wicketstuff.openlayers3.api.coordinate.LongLat;

/**
 * Provides an object that models an overlay containing a popover.
 */
public class Popover extends Overlay {

    private final static Logger logger = LoggerFactory.getLogger(Popover.class);

    /**
     * The placement position of the popover relative to the overlay's position.
     */
    private String placement;

    /**
     * Flag indicating that the content to be displayed is HTML.
     */
    private boolean html = true;

    /**
     * Backing model for this popover's title.
     */
    private IModel<String> titleModel;

    /**
     * Backing model for this popover.
     */
    private IModel<String> model;

    /**
     * Creates a new instance.
     *
     * @param component
     *         Target component for the overlay
     */
    public Popover(Component component, IModel<String> titleModel, IModel<String> model) {
        super(component);
        this.titleModel = titleModel;
        this.model = model;
    }

    /**
     * Returns the placement of this popover.
     *
     * @return String with placement value
     */
    public String getPlacement() {
        return placement;
    }

    /**
     * Sets the placement of this popover.
     *
     * @param placement
     *         New value
     */
    public void setPlacement(String placement) {
        this.placement = placement;
    }

    /**
     * Sets the placement of this popover.
     *
     * @param placement
     *         New value
     * @return This instance
     */
    public Popover placement(String placement) {
        setPlacement(placement);
        return this;
    }

    /**
     * Returns the value of the HTML flag for this popover.
     *
     * @return Value of the HTML flag
     */
    public boolean getHtml() {
        return html;
    }

    /**
     * Sets the value of the HTML flag for this popover.
     *
     * @param html
     *         New value
     */
    public void setHtml(boolean html) {
        this.html = html;
    }

    /**
     * Sets the value of the HTML flag for this popover.
     *
     * @param html
     *         New value
     * @return This instance
     */
    public Popover html(boolean html) {
        setHtml(html);
        return this;
    }

    /**
     * Returns the model for this popover.
     *
     * @return Backing model for the popover
     */
    public IModel<String> getModel() {
        return model;
    }

    /**
     * Sets the model for this popover.
     *
     * @param model
     *         New value
     */
    public void setModel(IModel<String> model) {
        this.model = model;
    }

    /**
     * Sets the model for this popover.
     *
     * @param model
     *         New value
     * @return This instance
     */
    public Popover model(IModel<String> model) {
        setModel(model);
        return this;
    }

    /**
     * Sets the position of the overlay.
     *
     * @param position
     *         Coordinate for the overlay's position
     * @return This instance
     */
    public Popover position(LongLat position) {
        setPosition(position);
        return this;
    }

    /**
     * Sets the positioning of this overlay.
     *
     * @param positioning
     *         New value
     * @return This instance
     */
    public Popover positioning(Positioning positioning) {
        setPositioning(positioning);
        return this;
    }

    /**
     * Sets the element tied to this popover.
     *
     * @param element
     *         Wicket component
     * @return This instance
     */
    public Popover element(Component element) {
        setElement(element);
        return this;
    }

    /**
     * Sets the stopEvent value.
     *
     * @param stopEvent
     *         New value
     * @return this instance
     */
    public Popover stopEvent(Boolean stopEvent) {
        setStopEvent(stopEvent);
        return this;
    }

    /**
     * Displays the popover.
     *
     * @param target
     *         Ajax request target
     * @return This instance
     */
    public Popover show(AjaxRequestTarget target) {
        target.appendJavaScript(renderPopoverJs());
        hide(target);
        target.appendJavaScript("$(" + getJsId() + ".getElement()).popover('show');");
        return this;
    }

    /**
     * Hides this popover.
     *
     * @param target
     *         Ajax request target
     * @return This instance
     */
    public Popover hide(AjaxRequestTarget target) {
        target.appendJavaScript("$(" + getJsId() + ".getElement()).popover('hide');");
        return this;
    }

    @Override
    public String getJsId() {
        return "popup_" + element.getMarkupId();
    }

    /**
     * Renders the Javascript for the popover.
     *
     * @return String with the rendered Javascript
     */
    private String renderPopoverJs() {

        StringBuilder builder = new StringBuilder();

        builder.append("var popup = " + getJsId() + ";");
        builder.append("var element = popup.getElement();");

        builder.append("$(element).popover('destroy');");
        builder.append("popup.setPosition(" + position.renderJs() + ");");

        builder.append("$(element).popover({");

        if (titleModel != null) {
            builder.append("'title': '" + escapeQuoteJs(titleModel.getObject()) + "',");
        }

        if (placement != null) {
            builder.append("'placement': '" + placement + "',");
        }

        builder.append("'html': '" + html + "',");
        if (html) {
            builder.append("'content': '" + escapeQuoteJs(model.getObject()) + "',");
        } else {
            builder.append("'content': '" + Strings.escapeMarkup(model.getObject()) + "',");
        }

        builder.append("});");

        if(titleModel != null) {
            builder.append("$(element).attr('data-original-title', '" + escapeQuoteJs(titleModel.getObject())  + "');" );
        }

        if (html) {
            builder.append("$(element).attr('data-content', '" + escapeQuoteJs(model.getObject()) + "');");
        } else {
            builder.append("$(element).attr('data-content', '" + Strings.escapeMarkup(model.getObject()) + "');");
        }

        return builder.toString();
    }
}
