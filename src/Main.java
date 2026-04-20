import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        // Run the UI on the Event Dispatch Thread for thread safety
        SwingUtilities.invokeLater(ProductGallery::new);
    }
}

class ProductGallery extends JFrame {
    private FadeImageViewer leftPanel;
    private JPanel rightPanel;
    private JLabel mainTitleLabel;
    private JLabel mainPriceLabel;

    public ProductGallery() {
        setTitle("E-Commerce - Product Gallery");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(Color.WHITE);

        // --- Header Section ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(240, 240, 240));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        JLabel titleLabel = new JLabel("Featured Products");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerPanel.add(titleLabel, BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);

        // --- Left Section (Main Product Image + Details) ---
        JPanel leftContainer = new JPanel(new BorderLayout());
        leftContainer.setBackground(Color.WHITE);
        leftContainer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 20, 20, 10),
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1)
        ));

        // Fade in/out image viewer
        leftPanel = new FadeImageViewer();
        leftContainer.add(leftPanel, BorderLayout.CENTER);

        // Main product info (Description and Price)
        JPanel mainInfoPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        mainInfoPanel.setBackground(Color.WHITE);
        mainInfoPanel.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        
        mainTitleLabel = new JLabel("Product Name - Description");
        mainTitleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        mainTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        mainPriceLabel = new JLabel("Price: ");
        mainPriceLabel.setFont(new Font("Arial", Font.BOLD, 20));
        mainPriceLabel.setForeground(new Color(220, 20, 60)); // Crimson Red
        mainPriceLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        mainInfoPanel.add(mainTitleLabel);
        mainInfoPanel.add(mainPriceLabel);
        leftContainer.add(mainInfoPanel, BorderLayout.SOUTH);

        add(leftContainer, BorderLayout.CENTER);

        // --- Right Section (Product Cards List) ---
        rightPanel = new JPanel(new GridLayout(0, 2, 15, 15));
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 20));
        
        String[] imagePaths = {"img1.png", "img2.png", "img3.png", "img4.png", "img5.png", "img6.png"};
        
        // Mock data for English descriptions and USD prices
        String[] productNames = {
            "Product 1 - Premium Imported", 
            "Product 2 - Modern Design", 
            "Product 3 - Latest 2024 Model", 
            "Product 4 - Eco-friendly Material", 
            "Product 5 - Special Offer", 
            "Product 6 - Limited Edition"
        };
        String[] productPrices = {
            "$150.00", 
            "$225.00", 
            "$345.00", 
            "$132.00", 
            "$885.00", 
            "$512.00"
        };
        
        for (int i = 0; i < imagePaths.length; i++) {
            try {
                File imgFile = new File(imagePaths[i]);
                if (!imgFile.exists()) {
                    continue; // Skip if file not found
                }
                BufferedImage img = ImageIO.read(imgFile);
                
                String pName = productNames[i];
                String pPrice = productPrices[i];
                
                // Show the first product by default
                if (i == 0) {
                    leftPanel.setImage(img);
                    mainTitleLabel.setText(pName);
                    mainPriceLabel.setText("Price: " + pPrice);
                }
                
                // Create a Card JPanel for each product
                JPanel itemCard = new JPanel(new BorderLayout());
                itemCard.setBackground(Color.WHITE);
                
                // Set constant border thickness (2px) to prevent layout jumping on hover
                itemCard.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230), 2));
                
                // Thumbnail button
                JButton btn = new JButton(new ImageIcon(getScaledImage(img, 140, 140)));
                btn.setBackground(Color.WHITE);
                btn.setFocusPainted(false);
                btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
                btn.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                
                final BufferedImage finalImg = img;
                
                // Click event to update main product
                btn.addActionListener(e -> {
                    leftPanel.setImage(finalImg);
                    mainTitleLabel.setText(pName);
                    mainPriceLabel.setText("Price: " + pPrice);
                });
                
                // Smooth hover effect by keeping border size constant and just changing colors
                btn.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        itemCard.setBorder(BorderFactory.createLineBorder(new Color(0, 120, 215), 2));
                        // Subtle background tint on hover
                        itemCard.setBackground(new Color(248, 250, 255));
                        btn.setBackground(new Color(248, 250, 255));
                    }
                    @Override
                    public void mouseExited(MouseEvent e) {
                        itemCard.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230), 2));
                        itemCard.setBackground(Color.WHITE);
                        btn.setBackground(Color.WHITE);
                    }
                });
                
                // Info panel for each product thumbnail
                JPanel infoPanel = new JPanel();
                infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
                infoPanel.setOpaque(false); // Make transparent so hover background is visible
                infoPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 10, 5));
                
                JLabel lblTitle = new JLabel("<html><center>" + pName + "</center></html>");
                lblTitle.setFont(new Font("Arial", Font.PLAIN, 12));
                lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
                
                JLabel lblPrice = new JLabel(pPrice);
                lblPrice.setForeground(Color.RED);
                lblPrice.setFont(new Font("Arial", Font.BOLD, 14));
                lblPrice.setAlignmentX(Component.CENTER_ALIGNMENT);
                
                infoPanel.add(lblTitle);
                infoPanel.add(Box.createVerticalStrut(5));
                infoPanel.add(lblPrice);
                
                itemCard.add(btn, BorderLayout.CENTER);
                itemCard.add(infoPanel, BorderLayout.SOUTH);
                
                rightPanel.add(itemCard);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        // Wrap right panel in a ScrollPane just in case
        JScrollPane scrollPane = new JScrollPane(rightPanel);
        scrollPane.setPreferredSize(new Dimension(420, 0));
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.EAST);
        
        // --- Footer Section ---
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(240, 240, 240));
        footerPanel.add(new JLabel("© 2026 Product Gallery - Lab 3 - 23520314"));
        add(footerPanel, BorderLayout.SOUTH);

        setSize(1000, 720);
        setLocationRelativeTo(null); // Center window
        setVisible(true);
    }
    
    // Helper method to resize images
    private Image getScaledImage(BufferedImage srcImg, int w, int h) {
        int imgW = srcImg.getWidth();
        int imgH = srcImg.getHeight();
        double scale = Math.min((double) w / imgW, (double) h / imgH);
        int scaledW = (int) (scale * imgW);
        int scaledH = (int) (scale * imgH);
        
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        
        int x = (w - scaledW) / 2;
        int y = (h - scaledH) / 2;
        
        g2.drawImage(srcImg, x, y, scaledW, scaledH, null);
        g2.dispose();
        return resizedImg;
    }
}

// Custom panel to draw image and handle Fade In/Out animations
class FadeImageViewer extends JPanel {
    private BufferedImage currentImage;
    private BufferedImage nextImage;
    private float alpha = 1.0f;
    private Timer timer;

    public FadeImageViewer() {
        setPreferredSize(new Dimension(500, 450));
        setBackground(Color.WHITE);
    }

    public void setImage(BufferedImage newImage) {
        if (currentImage == null) {
            currentImage = newImage;
            repaint();
            return;
        }
        if (currentImage == newImage) {
            return;
        }
        
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }
        
        nextImage = newImage;
        alpha = 0.0f;
        
        timer = new Timer(30, e -> {
            alpha += 0.05f;
            if (alpha >= 1.0f) {
                alpha = 1.0f;
                currentImage = nextImage;
                nextImage = null;
                timer.stop();
            }
            repaint();
        });
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        
        if (currentImage != null && alpha < 1.0f) {
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f - alpha));
            drawScaledImage(currentImage, g2d);
        } else if (currentImage != null && nextImage == null) {
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
            drawScaledImage(currentImage, g2d);
        }

        if (nextImage != null) {
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            drawScaledImage(nextImage, g2d);
        }
        
        g2d.dispose();
    }

    private void drawScaledImage(BufferedImage img, Graphics2D g2d) {
        int padding = 15;
        int w = getWidth() - 2 * padding;
        int h = getHeight() - 2 * padding;
        int imgW = img.getWidth();
        int imgH = img.getHeight();
        
        double scale = Math.min((double) w / imgW, (double) h / imgH);
        int scaledW = (int) (scale * imgW);
        int scaledH = (int) (scale * imgH);
        
        int x = padding + (w - scaledW) / 2;
        int y = padding + (h - scaledH) / 2;
        
        g2d.drawImage(img, x, y, scaledW, scaledH, null);
    }
}
