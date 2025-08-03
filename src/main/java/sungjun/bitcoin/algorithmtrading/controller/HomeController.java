package sungjun.bitcoin.algorithmtrading.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 홈 페이지와 메인 대시보드를 담당하는 컨트롤러입니다.
 * <p>
 * Thymeleaf 템플릿을 사용하여 서버 사이드 렌더링된 페이지를 제공합니다.
 * </p>
 *
 * @author sungjun
 * @since 1.0
 */
@Controller
public class HomeController {
    /**
     * 메인 대시보드 페이지를 반환합니다.
     *
     * @param model Thymeleaf 템플릿에 데이터를 전달하기 위한 모델
     * @return 대시보드 템플릿 경로
     */
    @GetMapping("/")
    public String dashboard(Model model) {
        model.addAttribute("title", "암호화폐 통합 거래 플랫폼");
        return "dashboard";
    }

}
