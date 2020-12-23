import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class Day22 extends JFrame {
    private final JLabel lblTime = new JLabel("Time: ");
    private final JPanel pnlPippin = new JPanel() {
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            drawPippin(g);
        }
    };
    private final JPanel pnlWilli = new JPanel() {
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            drawWilli(g);
        }
    };
    private final JPanel pnlLights1 = new JPanel() {
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            drawLights(g);
        }
    };
    private final JPanel pnlLights2 = new JPanel() {
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            drawLights(g);
        }
    };
    private final JPanel pnlLights3 = new JPanel() {
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            drawLights(g);
        }
    };
    private final JTextField tfStartSeconds = new JTextField();
    private final JLabel lblPippin = new JLabel("Pippin: 0m");
    private final JLabel lblWilli = new JLabel("Willi: 0m");
    private final JLabel lblOutput = new JLabel("Output: ");
    private final JLabel lblLights = new JLabel("Switch in ");
    private final JButton btnStart = new JButton("Start");
    private BufferedImage imagePippin, imageWilli, imageLights;
    private boolean running = false, isWaitingPippin = false, isWaitingWilli = false;
    private Thread threadAnimation;

    // Project Variables
    private final int durationGreenPhase = 25, durationYellowRedPhase = 35, distanceBetween = 600;
    private String phase = "Green", outputText = "<html>Output:<br>";
    private int time = 0, timeSinceStart = 0, distancePippin = 0, distanceWilli = 0;
    private int trafficLightSecondsLeft = 0;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Day22 frame = new Day22();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Create the frame
     */
    public Day22() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // close operation
        setBounds(10, 10, 400, 600); // standard size (if not fullscreen)
        JPanel contentPane = new JPanel() {
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawPanel(g);
            }
        };
        contentPane.setLayout(null); // mandatory for creating the layout
        setContentPane(contentPane);

        threadAnimation = new Thread(this::startAnimation);
        JLabel lblStartSeconds = new JLabel("Green seconds: ");
        lblStartSeconds.setBounds(10, 10, 100, 20);
        lblTime.setBounds(10, 40, 100, 20);
        tfStartSeconds.setBounds(120, 10, 100, 20);
        btnStart.setBounds(230, 10, 100, 20);
        btnStart.addActionListener(e -> {
            if (running) {
                threadAnimation.stop();
            } else {
                trafficLightSecondsLeft = checkInputDay22(tfStartSeconds.getText(), durationGreenPhase);
                if (trafficLightSecondsLeft > -1) {
                    threadAnimation.start();
                }
            }
        });

        lblOutput.setBounds(15, 325, 400, 50);
        lblLights.setBounds(300, 75, 100, 50);
        pnlLights1.setBounds(0, 75, 30, 50);
        pnlLights2.setBounds(120, 75, 30, 50);
        pnlLights3.setBounds(240, 75, 30, 50);
        lblPippin.setBounds(300, 190, 100, 20);
        lblWilli.setBounds(300, 290, 100, 20);
        pnlPippin.setBounds(-20, 180, 70, 40);
        pnlWilli.setBounds(-20, 280, 70, 40);
        try {
            imageLights = ImageIO.read(new File("./res/lights_yellowred.png"));
            imagePippin = ImageIO.read(new File("./res/car_Pippin.png"));
            imageWilli = ImageIO.read(new File("./res/car_Willi.png"));
        } catch (Exception e) {
            System.err.println("Image could not be loaded: " + e);
        }
        contentPane.add(lblStartSeconds);
        contentPane.add(lblOutput);
        contentPane.add(lblLights);
        contentPane.add(lblTime);
        contentPane.add(tfStartSeconds);
        contentPane.add(pnlPippin);
        contentPane.add(pnlWilli);
        contentPane.add(pnlLights1);
        contentPane.add(pnlLights2);
        contentPane.add(pnlLights3);
        contentPane.add(lblPippin);
        contentPane.add(lblWilli);
        contentPane.add(btnStart);
    }

    private void startAnimation() {
        outputText += "When P. and W. arrive at light 1 -> " + trafficLightSecondsLeft + " seconds left of green phase<br>";
        lblOutput.setText(outputText + "</html>");
        System.out.println("When Pippin and Willi arrive at traffic lights 1, there are " + trafficLightSecondsLeft + " seconds left of green phase");
        while (running) {
            try {
                if (!isWaitingPippin)
                    distancePippin += 20; // 20 m/s
                if (!isWaitingWilli)
                    distanceWilli += 10; // 10 m/s
                // If the traffic lights have to switch
                if (time == trafficLightSecondsLeft) {
                    time = 0; // Reset time
                    if (phase.equals("Green")) {
                        phase = "Yellow/Red";
                        imageLights = ImageIO.read(new File("./res/lights_yellowred.png"));
                        trafficLightSecondsLeft = durationYellowRedPhase;
                    } else {
                        phase = "Green";
                        imageLights = ImageIO.read(new File("./res/lights_green.png"));
                        trafficLightSecondsLeft = durationGreenPhase;
                    }
                    pnlLights1.repaint();
                    System.out.println("Output: Traffic lights switch to " + phase);
                }
                if (distancePippin % distanceBetween == 0) {
                    // Arrived at traffic light
                    if (!isWaitingPippin) {
                        outputText += "Pippin arrived at light " + (distancePippin / distanceBetween + 1) + ". It is " + phase + "<br>";
                        lblOutput.setText(outputText);
                    }
                    isWaitingPippin = phase.equals("Yellow/Red");
                }
                if (distanceWilli % distanceBetween == 0) {
                    // Arrived at traffic light
                    if (!isWaitingWilli) {
                        outputText += "Willi arrived at light " + (distancePippin / distanceBetween + 1) + ". It is " + phase + "<br>";
                        lblOutput.setText(outputText);
                    }
                    isWaitingWilli = phase.equals("Yellow/Red");
                }
                if (distancePippin == 1300 || distanceWilli == 1300) {
                    resetData();
                    break;
                }
                time++;
                timeSinceStart++;
                Thread.sleep(500); // wait 1 second
                pnlPippin.setBounds(distancePippin / 5 - 20, 180, 70, 40);
                lblPippin.setText("Pippin: " + distancePippin + "m");
                pnlWilli.setBounds(distanceWilli / 5 - 20, 280, 70, 40);
                lblWilli.setText("Willi: " + distanceWilli + "m");
                lblLights.setText("Switch in " + (trafficLightSecondsLeft - time) + "s");
                lblTime.setText("Time " + timeSinceStart + " s");
                super.update(this.getGraphics());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * If iteration is over, reset all Values
     */
    private void resetData() {
        btnStart.setText("Start");
        running = false;
        phase = "Green";
        time = 0;
        timeSinceStart = 0;
        distancePippin = 0;
        distanceWilli = 0;
        trafficLightSecondsLeft = 0;
    }

    private int checkInputDay22(String input, int durationGreenPhase) {
        int trafficLightSecondsLeft;
        try {
            trafficLightSecondsLeft = (durationGreenPhase - Integer.parseInt(input));
            if (trafficLightSecondsLeft < 0 || trafficLightSecondsLeft > 25) {
                tfStartSeconds.setBackground(Color.RED);
                tfStartSeconds.setText(input + " (0<x<" + durationGreenPhase + ")");
                trafficLightSecondsLeft = -1;
            } else {
                tfStartSeconds.setBackground(Color.WHITE);
                try {
                    imageLights = ImageIO.read(new File("./res/lights_green.png"));
                    pnlLights1.repaint();
                    running = !running;
                    btnStart.setText("Stop");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            super.update(this.getGraphics());
        } catch (Exception ex) {
            trafficLightSecondsLeft = durationGreenPhase;
            try {
                imageLights = ImageIO.read(new File("./res/lights_green.png"));
                pnlLights1.repaint();
                running = !running;
                btnStart.setText("Stop");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return trafficLightSecondsLeft;
    }

    /**
     * Draw every time the panel gets reloaded
     *
     * @param g The graphics to be drawn
     */
    public void drawPanel(Graphics g) {
        g.drawLine(15, 125, 15, 300);
        g.drawLine(135, 125, 135, 300);
        g.drawLine(255, 125, 255, 300);
    }

    /**
     * Draw every time the panel of Pippin gets reloaded
     *
     * @param g The graphics to be drawn
     */
    public void drawPippin(Graphics g) {
        g.drawImage(imagePippin, 0, 0, pnlPippin.getWidth(), pnlPippin.getHeight(), this); // see javadoc for more info on the parameters
    }

    /**
     * Draw every time the panel of Willi gets reloaded
     *
     * @param g The graphics to be drawn
     */
    public void drawWilli(Graphics g) {
        g.drawImage(imageWilli, 0, 0, pnlWilli.getWidth(), pnlWilli.getHeight(), this); // see javadoc for more info on the parameters
    }

    /**
     * Draw every time the panel of the traffic lights gets reloaded
     *
     * @param g The graphics to be drawn
     */
    public void drawLights(Graphics g) {
        g.drawImage(imageLights, 0, 0, pnlLights1.getWidth(), pnlLights1.getHeight(), this); // see javadoc for more info on the parameters
    }
}
