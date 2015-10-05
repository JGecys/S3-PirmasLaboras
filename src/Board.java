import javax.swing.*;
import java.awt.*;

/**
 * Created by Jurgis on 2015-09-10.
 */
public class Board extends JPanel {
    private Rectangle mPadding;


    public Board() {
        mPadding = new Rectangle(0, 0, 0, 0);
    }

    public void setPadding(Rectangle rectangle){
        mPadding = rectangle;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(getForeground());
        g.fillRect(mPadding.x, mPadding.y, getWidth() - 30, getHeight() - 30);
    }

}
