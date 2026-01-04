package util;

import java.io.File;
import java.util.logging.Level;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportEngineFactory;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.PDFRenderOption;

/**
 * Utility class untuk generate report menggunakan BIRT Report Engine
 */
public class BirtReportGenerator {

    private static IReportEngine engine = null;
    private static final String REPORT_DESIGN = "../report/subscription_report.rptdesign";

    /**
     * Initialize BIRT Report Engine
     */
    public static void initEngine() throws Exception {
        if (engine != null)
            return;

        EngineConfig config = new EngineConfig();
        config.setLogConfig(null, Level.OFF);

        Platform.startup(config);

        IReportEngineFactory factory = (IReportEngineFactory) Platform
                .createFactoryObject(IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY);

        engine = factory.createReportEngine(config);
    }

    /**
     * Shutdown BIRT Engine
     */
    public static void shutdownEngine() {
        if (engine != null) {
            engine.destroy();
            Platform.shutdown();
            engine = null;
        }
    }

    /**
     * Generate PDF Report dengan file chooser
     */
    public static void generatePDFWithDialog() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Export ke PDF");
        chooser.setFileFilter(new FileNameExtensionFilter("PDF Files", "pdf"));
        chooser.setSelectedFile(new File("laporan_langganan.pdf"));

        if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            String filePath = chooser.getSelectedFile().getAbsolutePath();
            if (!filePath.endsWith(".pdf"))
                filePath += ".pdf";
            generatePDF(filePath);
        }
    }

    /**
     * Generate HTML Report dengan file chooser
     */
    public static void generateHTMLWithDialog() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Export ke HTML");
        chooser.setFileFilter(new FileNameExtensionFilter("HTML Files", "html"));
        chooser.setSelectedFile(new File("laporan_langganan.html"));

        if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            String filePath = chooser.getSelectedFile().getAbsolutePath();
            if (!filePath.endsWith(".html"))
                filePath += ".html";
            generateHTML(filePath);
        }
    }

    /**
     * Generate PDF Report
     */
    public static void generatePDF(String outputPath) {
        try {
            initEngine();

            IReportRunnable design = engine.openReportDesign(REPORT_DESIGN);
            IRunAndRenderTask task = engine.createRunAndRenderTask(design);

            PDFRenderOption options = new PDFRenderOption();
            options.setOutputFileName(outputPath);
            options.setOutputFormat("pdf");

            task.setRenderOption(options);
            task.run();

            // Check for errors in task
            if (task.getErrors() != null && !task.getErrors().isEmpty()) {
                StringBuilder errors = new StringBuilder();
                for (Object err : task.getErrors()) {
                    errors.append(err.toString()).append("\n");
                }
                task.close();
                throw new Exception("Report errors: " + errors.toString());
            }

            task.close();

            JOptionPane.showMessageDialog(null,
                    "PDF berhasil dibuat:\n" + outputPath,
                    "Export Berhasil", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            e.printStackTrace();
            String message = e.getMessage();
            if (e.getCause() != null) {
                message += "\nCause: " + e.getCause().getMessage();
            }
            JOptionPane.showMessageDialog(null,
                    "Error generating PDF: " + message,
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Generate HTML Report
     */
    public static void generateHTML(String outputPath) {
        try {
            initEngine();

            IReportRunnable design = engine.openReportDesign(REPORT_DESIGN);
            IRunAndRenderTask task = engine.createRunAndRenderTask(design);

            HTMLRenderOption options = new HTMLRenderOption();
            options.setOutputFileName(outputPath);
            options.setOutputFormat("html");
            options.setEmbeddable(false);
            options.setHtmlPagination(false);

            task.setRenderOption(options);
            task.run();
            task.close();

            JOptionPane.showMessageDialog(null,
                    "HTML berhasil dibuat:\n" + outputPath,
                    "Export Berhasil", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Error generating HTML: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
