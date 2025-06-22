package com.stringcompare.stringcompare;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.*;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.stringcompare.stringcompare.dto.CompareRequest;
import com.stringcompare.stringcompare.dto.CompareResponse;

@RestController
@RequestMapping("/compare")
@CrossOrigin(origins = "http://localhost:4200")
public class StringcompareController {

    private String predefinedString = "tiger";

    @PostMapping
    public CompareResponse comparewithPredefined(@RequestBody CompareRequest request){
        String input = request.getInput().toLowerCase();
        String target = predefinedString.toLowerCase();
        //List<String> result = new ArrayList<>();

        List<Map<String, String>> status = new ArrayList<>();

        int len = Math.min(input.length(), predefinedString.length());

        //Count occurences of each char in predefined string
        Map<Character, Integer> charCount = new HashMap<>();
        /*for (int i =0; i < len; i++){
            char c1 = predefinedString.charAt(i);
            char c2 = input.charAt(i);

            if (c1 == c2){
                result.add("Match at index " + i + ": '" + c1 + "'");
            }else{
                result.add("Mismatch at index "+ i + ": '" + c1 + "' !='" + c2 + "'");
            }
        }*/
        for (char c: target.toCharArray()){
            charCount.put(c, charCount.getOrDefault(c, 0)+1);
        }
        /*if (input.length()!=predefinedString.length()){
            result.add("Strings have different lengths.");
        }
        return new CompareResponse(result, input.length() == predefinedString.length());*/
        //First pass: mark correct chars and reduce count
        String[] results = new String[input.length()];
        for (int i=0; i<input.length(); i++){
            char cInput = input.charAt(i);
            char cTarget = i < target.length() ? target.charAt(i) : '\0';

            if (cInput == cTarget){
                results[i] = "correct";
                charCount.put(cInput, charCount.get(cInput) -1);
            }else{
                results[i]="";
            }
        }

        //Second pass: Mark misplaced and wrong
        for (int i=0; i<input.length(); i++){
            if (results[i].equals("correct")) continue;

            char cInput = input.charAt(i);
            if (charCount.getOrDefault(cInput, 0)>0){
                results[i] = "misplaced";
                charCount.put(cInput, charCount.get(cInput) -1);
            }else{
                results[i] = "wrong";
            }
        }
        //Build response list
        for (int i=0; i<input.length(); i++){
            Map<String, String> entry = new HashMap<>();
            entry.put("char", String.valueOf(input.charAt(i)));
            entry.put("result", results[i]);
            status.add(entry);
        }
        //Determine if input matches exactly
        boolean complete = input.equals(target);
        return new CompareResponse(status, complete);
    }

    @PostMapping("/set-word")
    public void setWord(@RequestBody Map<String, String> body) {
        this.predefinedString = body.getOrDefault("word", "tiger").toLowerCase();
        System.out.println("🔁 Word updated to: " + this.predefinedString);
    }
}

/*Request to update the word
  curl -X POST http://localhost:8080/compare/set-word \
  -H "Content-Type: application/json" \
  -d '{"word": "clean"}'
 */