package swing;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
public class ScrollPaneDemo extends JFrame implements MouseListener {
        ImagePanel jp;
        
        JScrollPane jsp;
        public ScrollPaneDemo() {
                super("JScrollPane Demo");
                jp = new ImagePanel();
                jp.setPreferredSize(new Dimension(600, 400));
                jsp = new JScrollPane(jp);
                jp.addMouseListener(this);
                getContentPane().add(jsp);
                setSize(640, 480);
                setDefaultCloseOperation(EXIT_ON_CLOSE);
                setVisible(true);
        }
        public void mouseClicked(MouseEvent e) {
                System.out.println("mouseClicked");
                jp.enlarge();
                jp.setPreferredSize(jp.getPreferredSize());
                jsp.validate();
        }
        public void mousePressed(MouseEvent e) {
                // System.out.println("mousePressed");
        }
        public void mouseReleased(MouseEvent e) {
                // System.out.println("mouseReleased");
        }
        public void mouseEntered(MouseEvent e) {
                // System.out.println("mouseEntered");
        }
        public void mouseExited(MouseEvent e) {
                // System.out.println("mouseExited");
        }
        public static void main(String[] args) {
                new ScrollPaneDemo();
        }
}
class ImagePanel extends JPanel {
        private Image image;
        private Dimension theSize = new Dimension(600, 400);
        public void setImage(Image image) {
                this.image = image;
                this.repaint();
        }
        public Image getImage() {
                return this.image;
        }
        public void paintComponent(Graphics g) {
                ImageIcon img = new ImageIcon("Winter.jpg");
                g.drawImage(img.getImage(), 0, 0, theSize.width, theSize.height, null);
        }
        public void enlarge() {
                theSize.width = (theSize.width * 101) / 100;
                theSize.height = (theSize.height * 101) / 100;
                setSize(theSize);
        }
        
        public Dimension getPreferredSize() {
                return this.theSize;
        }
}