package deeplearningGroup1;

import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceRGB;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotation;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationTextMarkup;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;
/*
 * @Author Antonino Abeshi
 * 
 */
class MyAnnotator extends PDFTextStripper {

	// the text you want to highlight (or annotate, as it's called in PDF language)
	Jarvis jarvis = new Jarvis();
    final static String[] texts = { "Arse", "Strikeout", "Squiggly", "Highlight" };
    
    public MyAnnotator() throws IOException {
        super();
        
        //int []poop = jarvis.getCommentPositions();
    }

    /*
     * (non-Javadoc)
     * @see org.apache.pdfbox.text.PDFTextStripper#writeString(java.lang.String, java.util.List)
     */
    @Override
    protected void writeString (String string, List<TextPosition> textPositions) throws IOException {
        float posXInit = 0, posXEnd = 0, posYInit = 0, posYEnd = 0, width = 0, height = 0, fontHeight = 0;

        int foundTextNo = -1;
        for (int i = 0; i < texts.length; i++) {
            if (string.contains(texts[i]))  {
                foundTextNo = i;
                break;
            } 
        }
        if (foundTextNo != -1) {
            posXInit = textPositions.get(0).getXDirAdj();
            posXEnd  = textPositions.get(textPositions.size() - 1).getXDirAdj()
                       + textPositions.get(textPositions.size() - 1).getWidth();
            posYInit = textPositions.get(0).getPageHeight()
                       - textPositions.get(0).getYDirAdj();
            posYEnd  = textPositions.get(0).getPageHeight()
                       - textPositions.get(textPositions.size() - 1).getYDirAdj();
            width    = textPositions.get(0).getWidthDirAdj();
            height   = textPositions.get(0).getHeightDir();

            List<PDAnnotation> annotations = document.getPage(this.getCurrentPageNo() - 1).getAnnotations();
            PDAnnotationTextMarkup markup = null;
            // choose any color you want, they can be different for each annotation
            PDColor color = new PDColor(new float[]{ 1, 1 / 255F, 1 }, PDDeviceRGB.INSTANCE);
            switch (foundTextNo) {
                case 0:
                    markup = new PDAnnotationTextMarkup(PDAnnotationTextMarkup.SUB_TYPE_UNDERLINE);
                    break;
                case 1:
                    markup = new PDAnnotationTextMarkup(PDAnnotationTextMarkup.SUB_TYPE_STRIKEOUT);
                    break;
                case 2:
                    markup = new PDAnnotationTextMarkup(PDAnnotationTextMarkup.SUB_TYPE_SQUIGGLY);
                    break;
                case 3:
                    markup = new PDAnnotationTextMarkup(PDAnnotationTextMarkup.SUB_TYPE_HIGHLIGHT);
                    break;
            }

            PDRectangle position = new PDRectangle();
            position.setLowerLeftX(posXInit);
            position.setLowerLeftY(posYEnd);
            position.setUpperRightX(posXEnd);
            position.setUpperRightY(posYEnd + height);
            markup.setRectangle(position);

            float quadPoints[] = {posXInit, posYEnd + height + 2,
                                  posXEnd, posYEnd + height + 2,
                                  posXInit, posYInit - 2,
                                  posXEnd, posYEnd - 2};
            markup.setQuadPoints(quadPoints);

            markup.setColor(color);
            annotations.add(markup);
        }
    }
}

