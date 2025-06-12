package com.example.calculator;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@Controller
@RequestMapping("/calculator")
class CalculatorController {

    private final CalculatorService calculatorService = new CalculatorService();

    @GetMapping
    public String calculatorPage(Model model) {
        model.addAttribute("result", calculatorService.getCurrentResult());
        return "calculator";
    }

    @PostMapping("/compute")
    public String compute(@RequestParam String a, @RequestParam String b, @RequestParam String operator, Model model) {
        try {
            if (a.length() > 20 || b.length() > 20) {
                model.addAttribute("result", "錯誤：輸入數字過長，請輸入20位以內的數字");
                return "calculator";
            }

            if (!operator.matches("[+\\-*/]")) {
                model.addAttribute("result", "錯誤：不支援的運算子");
                return "calculator";
            }

            BigDecimal numA = new BigDecimal(a);
            BigDecimal numB = new BigDecimal(b);
            BigDecimal result = calculatorService.compute(numA, numB, operator);

            String formattedResult = result.stripTrailingZeros().toPlainString();
            model.addAttribute("result", formattedResult);
            model.addAttribute("inputA", numA.toPlainString());
            model.addAttribute("inputB", numB.toPlainString());
            model.addAttribute("operator", operator);
        } catch (NumberFormatException e) {
            model.addAttribute("result", "錯誤：請輸入正確的數字");
        } catch (ArithmeticException e) {
            model.addAttribute("result", "錯誤：" + e.getMessage());
        } catch (Exception e) {
            model.addAttribute("result", "發生未知錯誤");
        }
        return "calculator";
    }

    @PostMapping("/undo")
    public String undo(Model model) {
        BigDecimal result = calculatorService.undo();
        model.addAttribute("result", result.stripTrailingZeros().toPlainString());
        model.addAttribute("expression", calculatorService.getCurrentExpression());
        return "calculator";
    }

    @PostMapping("/redo")
    public String redo(Model model) {
        BigDecimal result = calculatorService.redo();
        model.addAttribute("result", result.stripTrailingZeros().toPlainString());
        model.addAttribute("expression", calculatorService.getCurrentExpression());
        return "calculator";
    }
}
