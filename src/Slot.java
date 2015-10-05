import com.sun.javafx.geom.Vec3d;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.*;
import java.util.List;

/**
 * Created by Jurgis on 2015-09-10.
 */
public class Slot extends JPanel {

    public static final int padding = 10;

    public static final Color[][] colors = {{Color.decode("#F5A9A9"), Color.decode("#FA5858")},
            {Color.decode("#58ACFA"), Color.decode("#2E64FE")}};

    public static final float[] xPoints = {0.1f, 0.2f, 0.9f, 0.9f, 0.8f, 0.1f};
    public static final float[] xPointsInverted = {0.9f, 0.8f, 0.1f, 0.1f, 0.2f, 0.9f};
    public static final float[] yPoints = {0.1f, 0.1f, 0.8f, 0.9f, 0.9f, 0.2f};

    public enum SlotType {TUSCIAS, KRYZIUS, NULIS}

    protected SlotType mSlotType = SlotType.TUSCIAS;
    private List<OnSymbolPlacedListener> listenerList;
    private Zaidimas mZaidimas;

    public Slot(Zaidimas zaidimas) {
        mZaidimas = zaidimas;
        listenerList = new ArrayList<>();
        addMouseListener(new SlotMouseAdapter());
    }

    public SlotType getSlotType() {
        return mSlotType;
    }

    public void setSlotType(SlotType type) {
        mSlotType = type;
    }

    public void addSymbolPlacedListener(OnSymbolPlacedListener listener) {
        listenerList.add(listener);
    }

    public void removeSymbolPlacedListener(OnSymbolPlacedListener listener) {
        listenerList.remove(listener);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = ((Graphics2D) g);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        switch (mSlotType) {
            case NULIS:
                g2.setColor(Color.RED);
                g2.fillOval(padding, padding, getWidth() - padding * 2, getHeight() - padding * 2);
                g2.setColor(getBackground());
                g2.fillOval(padding * 3, padding * 3, getWidth() - padding * 6, getHeight() - padding * 6);
                break;
            case KRYZIUS:
                Polygon one = new Polygon();
                Polygon two = new Polygon();
                for (int i = 0; i < xPoints.length; i++) {
                    one.addPoint((int) (xPoints[i] * getWidth()), (int) (yPoints[i] * getHeight()));
                    two.addPoint((int) (xPointsInverted[i] * getWidth()), (int) (yPoints[i] * getHeight()));
                }
                g2.setColor(Color.BLUE);
                g2.fillPolygon(one);
                g2.fillPolygon(two);

                break;
        }
    }

    @Override
    public void repaint() {
        super.repaint();
        super.revalidate();
    }

    private Color getBackgroundColorByType() {
        return colors[mZaidimas.getTurn()][0];
    }

    private Color getBackgroundColorByTypePressed() {
        return colors[mZaidimas.getTurn()][1];
    }

    public interface OnSymbolPlacedListener {
        void OnSymbolPlaced(Slot slot);
    }

    public class SlotMouseAdapter extends MouseAdapter {
        boolean isMouseInside = false;
        boolean isMousePressed = false;

        @Override
        public void mousePressed(MouseEvent e) {
            Zaidimas.isMouseDown = true;
            isMousePressed = true;
            if (mSlotType == SlotType.TUSCIAS) {
                setBackground(getBackgroundColorByTypePressed());
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            Zaidimas.isMouseDown = false;
            isMousePressed = false;
            if (!isMouseInside) {
                setBackground(Color.WHITE);
                repaint();
                return;
            }
            if (getSlotType() == SlotType.TUSCIAS) {
                mSlotType = mZaidimas.getTurnType();
                repaint();
                for (OnSymbolPlacedListener listener : listenerList) {
                    listener.OnSymbolPlaced(Slot.this);
                }
                setBackground(Color.WHITE);
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            if (getSlotType() == SlotType.TUSCIAS && !Zaidimas.isMouseDown) {
                Slot.this.setBackground(getBackgroundColorByType());
            }
            if (isMousePressed) {
                Slot.this.setBackground(getBackgroundColorByTypePressed());
            }
            isMouseInside = true;
        }

        @Override
        public void mouseExited(MouseEvent e) {
            setBackground(Color.WHITE);
            isMouseInside = false;
        }
    }
}
