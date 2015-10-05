import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by Jurgis on 2015-09-10.
 */
public class Zaidimas extends JFrame implements Slot.OnSymbolPlacedListener {
    public static final Object[] OPTIONS_VARIANTS = {"Taip", "Ne"};
    public static final int WINDOW_SIZE = 500;
    public static int n = 3;
    public static boolean isMouseDown = false;

    private JPanel mRootPanel;

    private int mTurn = 0;
    private int mMoveCount = 0;

    Slot[][] mSlots;

    public Zaidimas() {
        super("Kryziukai-Nuliukai");
        try{
            while( (n = Integer.parseInt(JOptionPane.showInputDialog("Pasirinkite lenteles dydi: ", "3"))) < 2 || n > 10);
        }catch (NumberFormatException e){
            close();
            return;
        }
        setResizable(false);
        setSize(WINDOW_SIZE, WINDOW_SIZE);
        Board mContainer = new Board();
        mSlots = new Slot[n][n];
        GridLayout mLayout = new GridLayout(n, n);
        mLayout.setHgap(4);
        mLayout.setVgap(4);
        mContainer.setLayout(mLayout);
        mContainer.setBackground(Color.WHITE);
        mContainer.setForeground(Color.BLACK);
        mContainer.setPadding(new Rectangle(16, 16, 16, 16));
        mRootPanel.setLayout(new GridLayout(1, 1));
        mRootPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mRootPanel.setBackground(Color.WHITE);
        for (int i = 0; i < n * n; i++) {
            Slot slot = new Slot(this);
            slot.addSymbolPlacedListener(this);
            slot.setBackground(Color.white);
            mContainer.add(slot);
            mSlots[i / n][i % n] = slot;
        }
        mRootPanel.add(mContainer);
        setContentPane(mRootPanel);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationByPlatform(true);
        setVisible(true);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Stats.saveStats();
            }
        });
    }

    public int getTurn(){
        return mTurn;
    }

    public Slot.SlotType getTurnType() {
        switch (mTurn) {
            case 1:
                return Slot.SlotType.KRYZIUS;
            case 0:
                return Slot.SlotType.NULIS;
        }
        return Slot.SlotType.KRYZIUS;
    }

    public void nextTurn() {
        mTurn = (mTurn + 1) % 2;
        mMoveCount += 1;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    public void reset() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                mSlots[i][j].setSlotType(Slot.SlotType.TUSCIAS);
                mSlots[i][j].setBackground(Color.WHITE);
                mSlots[i][j].repaint();
            }
        }
        mMoveCount = 0;
        mTurn = 0;
    }

    public boolean checkIfFinnished(Slot slot) {
        int x = 0;
        int y = 0;
        Slot.SlotType type = slot.getSlotType();
        Color background = Slot.colors[slot.getSlotType() == Slot.SlotType.KRYZIUS ? 1 : 0][0];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (mSlots[i][j] == slot) {
                    x = i;
                    y = j;
                }
            }
        }
        //check col
        for (int i = 0; i < n; i++) {
            if (mSlots[x][i].getSlotType() != type)
                break;
            if (i == n - 1) {
                for (int j = 0; j < n; j++){
                    mSlots[x][j].setBackground(background);
                }
                winner(slot);
                return true;
            }
        }

        //check row
        for (int i = 0; i < n; i++) {
            if (mSlots[i][y].getSlotType() != type)
                break;
            if (i == n - 1) {
                for (int j = 0; j < n; j++){
                    mSlots[j][y].setBackground(background);
                }
                winner(slot);
                return true;
            }
        }

        //check diag
        if (x == y) {
            //we're on a diagonal
            for (int i = 0; i < n; i++) {
                if (mSlots[i][i].getSlotType() != type)
                    break;
                if (i == n - 1) {
                    for (int j = 0; j < n; j++){
                        mSlots[j][j].setBackground(background);
                    }
                    winner(slot);
                    return true;
                }
            }
        }

        //check anti diag (thanks rampion)
        for (int i = 0; i < n; i++) {
            if (mSlots[i][(n - 1) - i].getSlotType() != type)
                break;
            if (i == n - 1) {
                for (int j = 0; j < n; j++){
                    mSlots[j][(n - 1) - j].setBackground(background);
                }
                winner(slot);
                return true;
            }
        }

        //check draw
        if (mMoveCount > ((n * n) - 1)) {
            draw(slot);
            return true;
        }
        return false;
    }

    void winner(Slot slot) {
        if(slot.getSlotType() == Slot.SlotType.KRYZIUS){
            Stats.mCrossWins++;
        }else{
            Stats.mCircleWins++;
        }
        int n = JOptionPane.showOptionDialog(this,
                "Laimejo " + ((slot.getSlotType() == Slot.SlotType.KRYZIUS) ? "Kryziukai" : "Nuliukai") +
                        "\nRezultatas:\nKryziukai " + Stats.mCrossWins + ":" + Stats.mCircleWins +
                        " Nuliukai\nLygiosios: " + Stats.mDraws,
                "Ar norite zaisti dar karta?",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                OPTIONS_VARIANTS,
                OPTIONS_VARIANTS[0]);
        switch (n) {
            case JOptionPane.YES_OPTION:
                reset();
                break;
            case JOptionPane.NO_OPTION:
                this.close();
                break;
            case JOptionPane.CLOSED_OPTION:
                this.close();
                break;
        }
        reset();
    }

    void draw(Slot slot) {
        Stats.mDraws++;
        slot.setBackground(Color.WHITE);
        int n = JOptionPane.showOptionDialog(this,
                "Ar norite zaisti dar karta?",
                "Lygiosios",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                OPTIONS_VARIANTS,
                OPTIONS_VARIANTS[0]);
        switch (n) {
            case JOptionPane.YES_OPTION:
                reset();
                break;
            case JOptionPane.NO_OPTION:
                this.close();
                break;
            case JOptionPane.CLOSED_OPTION:
                this.close();
                break;
        }
    }

    void close() {
        dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }

    @Override
    public void OnSymbolPlaced(Slot slot) {
        nextTurn();
        if(checkIfFinnished(slot)) return;
    }
}
