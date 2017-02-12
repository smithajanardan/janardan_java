package ClassExamples;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class EventExampleMain {
    public static void main(String[] args) { 
        EventExampleFrame f = new EventExampleFrame(); 
        f.start(); 
    } 
}

class EventExampleFrame extends JFrame {
    private EventExamplePanel panel;
    
    public EventExampleFrame(){
        //  set up frame 
        setTitle("Events Example");
        setSize(400, 400);
        setLocation(100, 100);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        panel = new EventExamplePanel();
        this.add(panel);
    }
    
    public void start(){
        setVisible(true);
    }
}

class EventExamplePanel extends JPanel {
    
    private static final String[] buttonNames 
        = {"Red", "Green", "Blue", "Pink", "Orange", "Gray", "White", "Black"};
    private static final Color[] buttonColors = {Color.RED, Color.GREEN,
        Color.BLUE, Color.PINK, Color.ORANGE, Color.GRAY, Color.WHITE,
        Color.BLACK};
    
    private Color currentColor;
    
    // private JButton[] buttons;
    
    public EventExamplePanel(){
        currentColor = Color.ORANGE;
        
       JButton[] buttons = new JButton[buttonNames.length];
        
//        System.out.println(buttons);
//        System.out.println(Arrays.toString(buttons));
        
        for(int i = 0; i < buttons.length; i++) {
            buttons[i] = new JButton(buttonNames[i]);
            // for seperate and nest classes we had
            // buttons[i].addActionListener(new ColorActionListener(this, buttonColors[i]));
            
            // for inner class we had
         // buttons[i].addActionListener(new ColorActionListener(buttonColors[i]));
            
            // use method that creates anonymous inner class
            buttons[i].addActionListener(
                    createButtonListener(buttonColors[i]));
            add(buttons[i]);
        }
    }
    
    public void paintComponent(Graphics g){
        super.paintComponent(g);       
        setBackground(currentColor);
        System.out.println("panel paint component called. currentColor: "
                + currentColor);
    }
    
    public void setColor(Color c) {
        currentColor = c;
    }
    
    private ActionListener createButtonListener(final Color c) {
        
        // anonymous inner class being created
        return new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setColor(c);
                repaint();
            }
        };
    }
    
    //  inner class
//    private class ColorActionListener implements ActionListener {
//
//        private Color color;
//        
//        public ColorActionListener(Color c) {
//            color = c;
//        }
//        
//        @Override
//        public void actionPerformed(ActionEvent event) {
//            setColor(color);
//            repaint();
//        }  
//        
//    } // end of ColorActionListener
    
    //  nested class class name is EventExamplePanel.ColorActionListener
//  private static class ColorActionListener implements ActionListener {
//
//      private EventExamplePanel panel
//      private Color color;
//      
//      public ColorActionListener(EventExamplePanel p, Color c) {
//          panel = p;
//          color = c;
//      }
//      
//      @Override
//      public void actionPerformed(ActionEvent event) {
//          panel.setColor(color);
//          panel.repaint();
//      }  
//      
//  } // end of ColorActionListener
    
} // end of EventExamplePanel

// seperate class in same file. class name is ColorActionListener
//class ColorActionListener implements ActionListener {
//
//      private EventExamplePanel panel
//      private Color color;
//      
//      public ColorActionListener(EventExamplePanel p, Color c) {
//          panel = p;
//          color = c;
//      }
//      
//      @Override
//      public void actionPerformed(ActionEvent event) {
//          panel.setColor(color);
//          panel.repaint();
//      }  
//      
//  } // end of ColorActionListener



