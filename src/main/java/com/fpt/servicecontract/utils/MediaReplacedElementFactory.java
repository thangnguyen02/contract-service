package com.fpt.servicecontract.utils;
import org.apache.commons.io.IOUtils;

import com.lowagie.text.Image;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.w3c.dom.Element;
import org.xhtmlrenderer.extend.FSImage;
import org.xhtmlrenderer.extend.ReplacedElement;
import org.xhtmlrenderer.extend.ReplacedElementFactory;
import org.xhtmlrenderer.extend.UserAgentCallback;
import org.xhtmlrenderer.layout.LayoutContext;
import org.xhtmlrenderer.pdf.ITextFSImage;
import org.xhtmlrenderer.pdf.ITextImageElement;
import org.xhtmlrenderer.render.BlockBox;
import org.xhtmlrenderer.simple.extend.FormSubmissionListener;


import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MediaReplacedElementFactory implements ReplacedElementFactory {

  private final ReplacedElementFactory superFactory;

  public MediaReplacedElementFactory(ReplacedElementFactory superFactory) {
    this.superFactory = superFactory;
  }

  @Override
  public ReplacedElement createReplacedElement(LayoutContext layoutContext, BlockBox blockBox,
      UserAgentCallback userAgentCallback, int cssWidth, int cssHeight) {
    Element element = blockBox.getElement();
    if (element == null) {
      return null;
    }
    String nodeName = element.getNodeName();
    if ("div".equals(nodeName) && element.hasAttribute("replace-type") &&
        element.getAttribute("replace-type").equals("media")
    ) {
      InputStream input = null;
      try {
        input =
            new ClassPathResource("imgs/" + element.getAttribute("data-src")).getInputStream();
        final byte[] bytes = IOUtils.toByteArray(input);
        final Image image = Image.getInstance(bytes);
        image.setSmask(true);
        final FSImage fsImage = new ITextFSImage(image);
        if ((cssWidth != -1) || (cssHeight != -1)) {
          fsImage.scale(cssWidth, cssHeight);
        }
        return new ITextImageElement(fsImage);
      } catch (Exception e) {
         e.printStackTrace();
      } finally {
        IOUtils.closeQuietly(input);
      }
    }
    return this.superFactory.createReplacedElement(layoutContext, blockBox, userAgentCallback,
        cssWidth, cssHeight);
  }

  @Override
  public void reset() {
    this.superFactory.reset();
  }

  @Override
  public void remove(Element e) {
    this.superFactory.remove(e);
  }

  @Override
  public void setFormSubmissionListener(FormSubmissionListener listener) {
    this.superFactory.setFormSubmissionListener(listener);
  }

  public void replaceCost(BlockBox blockBox, Element element) {
    if (element.hasAttribute("cost_type") &&
        element.getAttribute("cost_type").equals("cost")
    ) {
      String cost = element.getTextContent();
      Pattern pattern = Pattern.compile("[$€¥₩₽£฿₪]{1,3}");
      Matcher matcher = pattern.matcher(cost);
      String currency = "";
      if (matcher.find()) {
        currency = matcher.group();
      }
      if (StringUtils.isNotEmpty(currency)) {
        String currencyHtml = "<span style=\"font-family: 'Times New Roman'\">" + currency + "</span>";
        cost = cost.replace(currency, currencyHtml);
        element.setTextContent(cost);
        blockBox.setElement(element);
      }
    }
  }
}