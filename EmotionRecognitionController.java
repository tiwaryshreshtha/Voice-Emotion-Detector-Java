import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.ui.Model;
import org.nd4j.linalg.api.ndarray.INDArray;
@Controller
public class EmotionRecognitionController {
    @GetMapping("/")
    public String uploadForm() {
        return "upload";
    }

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("audioFile") MultipartFile file, Model model) {
        try {
            byte[] audioData = file.getBytes();
            INDArray features = extractFeatures(audioData);
            INDArray predictions = model.output(features, false);
            String emotion = mapPredictionsToEmotion(predictions);
            model.addAttribute("emotion", emotion);
        } catch (Exception e) {
            model.addAttribute("error", "Error processing the audio file.");
        }
        return "result";
    }
}
