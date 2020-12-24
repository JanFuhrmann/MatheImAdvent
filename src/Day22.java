import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

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
    private final JTextField tfStartSeconds = new JTextField();
    private final JTextArea txtOutput = new JTextArea("Output: ");
    private final JLabel lblPippin = new JLabel("Pippin: 0m");
    private final JLabel lblWilli = new JLabel("Willi: 0m");
    private final JLabel lblLights = new JLabel("Switch in ");
    private final JButton btnStart = new JButton("Start");
    private final JButton btnSpeed = new JButton("1x");
    private BufferedImage imagePippin, imageWilli, imageLights;
    private boolean running = false, started = false, isWaitingPippin = false, isWaitingWilli = false;
    private Thread threadAnimation;

    private String phase = "Green", outputText = "Output:\n";
    private int time = 0;
    private int timeSinceStart = 0;
    private int distancePippin = 0;
    private int distanceWilli = 0;
    private int animationSpeed = 1000;
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
        setTitle("MatheImAdvent No.22 by Jan Fuhrmann");
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

        // Declarations and Initializations
        JLabel lblStartSeconds = new JLabel("Green seconds: ");
        JPanel pnlLights2 = new JPanel() {
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawLights(g);
            }
        };
        JPanel pnlLights3 = new JPanel() {
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawLights(g);
            }
        };

        // Position and Size
        lblStartSeconds.setBounds(10, 10, 100, 20);
        lblTime.setBounds(10, 40, 100, 20);
        lblLights.setBounds(300, 75, 100, 50);
        lblPippin.setBounds(300, 190, 100, 20);
        lblWilli.setBounds(300, 290, 100, 20);
        tfStartSeconds.setBounds(110, 10, 100, 20);
        txtOutput.setBounds(15, 325, 385, 275);
        btnStart.setBounds(220, 10, 90, 20);
        btnSpeed.setBounds(320, 10, 55, 20);
        pnlLights1.setBounds(0, 75, 30, 50);
        pnlLights2.setBounds(120, 75, 30, 50);
        pnlLights3.setBounds(240, 75, 30, 50);
        pnlPippin.setBounds(-20, 180, 70, 40);
        pnlWilli.setBounds(-20, 280, 70, 40);

        // Listener
        btnStart.addActionListener(e -> {
            threadAnimation = new Thread(this::startAnimation);
            if (running) {
                running = false;
                btnStart.setText("Start");
                threadAnimation.stop();
            } else {
                running = true;
                btnStart.setText("Stop");
                if (!started) {
                    trafficLightSecondsLeft = checkInputDay22(tfStartSeconds.getText());
                }
                if (trafficLightSecondsLeft > -1) {
                    threadAnimation.start();
                }
            }
        });
        btnSpeed.addActionListener(e -> {
            if (animationSpeed == 1000) {
                animationSpeed = 500;
                btnSpeed.setText("2x");
            } else if (animationSpeed == 500) {
                animationSpeed = 250;
                btnSpeed.setText("4x");
            } else if (animationSpeed == 250) {
                animationSpeed = 100;
                btnSpeed.setText("10x");
            } else {
                animationSpeed = 1000;
                btnSpeed.setText("1x");
            }
        });

        try {
            //imageLights = ImageIO.read(new File("/res/lights_yellowred.png"));
            imageLights = ImageIO.read(getClass().getClassLoader().getResource("lights_yellowred.png"));
            imagePippin = ImageIO.read(getClass().getClassLoader().getResource("car_Pippin.png"));
            imageWilli = ImageIO.read(getClass().getClassLoader().getResource("car_Willi.png"));
        } catch (Exception e) {
            System.err.println("Image could not be loaded: " + e);
        }
        contentPane.add(lblStartSeconds);
        contentPane.add(txtOutput);
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
        contentPane.add(btnSpeed);
    }

    /**
     * Start the animation
     */
    private void startAnimation() {
        int timeUntilSwitch = trafficLightSecondsLeft - time;
        outputText += "There are " + timeUntilSwitch + " seconds left of " + phase + " phase\n";
        txtOutput.setText(outputText);
        System.out.println("There are " + timeUntilSwitch + " seconds left of " + phase + " phase");
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
                        imageLights = ImageIO.read(getClass().getClassLoader().getResource("lights_yellowred.png"));
                        trafficLightSecondsLeft = 35;
                    } else {
                        phase = "Green";
                        imageLights = ImageIO.read(getClass().getClassLoader().getResource("lights_green.png"));
                        // Project Variables
                        trafficLightSecondsLeft = 25;
                    }
                    pnlLights1.repaint();
                    System.out.println("Output: Traffic lights switch to " + phase);
                }
                int distanceBetween = 600;
                if (distancePippin % distanceBetween == 0) {
                    // Arrived at traffic light
                    if (!isWaitingPippin) {
                        outputText += "Pippin arrived at light " + (distancePippin / distanceBetween + 1) + ". It is " + phase + "\n";
                        txtOutput.setText(outputText);
                    }
                    isWaitingPippin = phase.equals("Yellow/Red");
                }
                if (distanceWilli % distanceBetween == 0) {
                    // Arrived at traffic light
                    if (!isWaitingWilli) {
                        outputText += "Willi arrived at light " + (distancePippin / distanceBetween + 1) + ". It is " + phase + "\n";
                        txtOutput.setText(outputText);
                    }
                    isWaitingWilli = phase.equals("Yellow/Red");
                }
                if (distancePippin > 1500 && distanceWilli > 1500) {
                    resetData();
                    break;
                }
                time++;
                timeSinceStart++;
                Thread.sleep(animationSpeed); // wait 1 second
                pnlPippin.setBounds(distancePippin / 5 - 20, 180, 70, 40);
                lblPippin.setText("Pippin: " + distancePippin + "m");
                pnlWilli.setBounds(distanceWilli / 5 - 20, 280, 70, 40);
                lblWilli.setText("Willi: " + distanceWilli + "m");
                timeUntilSwitch = trafficLightSecondsLeft - time;
                lblLights.setText("Switch in " + timeUntilSwitch + "s");
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
        btnStart.setText("Restart");
        running = false;
        started = false;
        phase = "Green";
        time = 0;
        timeSinceStart = 0;
        distancePippin = 0;
        distanceWilli = 0;
        trafficLightSecondsLeft = 0;
        isWaitingPippin = false;
        isWaitingWilli = false;
    }

    /**
     * First start, check if input is valid and return the duration of green phase
     *
     * @param input The input String
     * @return The duration of green phase
     */
    private int checkInputDay22(String input) {
        int trafficLightSecondsLeft;
        try {
            trafficLightSecondsLeft = (25 - Integer.parseInt(input));
            if (trafficLightSecondsLeft < 0 || trafficLightSecondsLeft > 25) {
                tfStartSeconds.setBackground(Color.RED);
                tfStartSeconds.setText(input + " (0<x<" + 25 + ")");
                trafficLightSecondsLeft = -1;
            } else {
                tfStartSeconds.setBackground(Color.WHITE);
                try {
                    imageLights = ImageIO.read(getClass().getClassLoader().getResource("lights_green.png"));
                    pnlLights1.repaint();
                    started = true;
                    btnStart.setText("Stop");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            super.update(this.getGraphics());
        } catch (Exception ex) {
            tfStartSeconds.setBackground(Color.RED);
            tfStartSeconds.setText(input + " (0<x<" + 25 + ")");
            trafficLightSecondsLeft = -1;
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
