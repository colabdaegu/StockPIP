package pip;

import java.util.List;
import java.util.Optional;

public class PipCloseManager {

    private final List<PipMain> pipWindows;

    // 기존 PipMain 리스트를 주입받아 관리
    public PipCloseManager(List<PipMain> pipWindows) {
        this.pipWindows = pipWindows;
    }

    // 티커로 PIP 창 닫기
    public boolean closePipByTicker(String ticker) {
        Optional<PipMain> optional = pipWindows.stream()
                .filter(p -> p.getStockTicker().equals(ticker))
                .findFirst();

        if (optional.isPresent()) {
            PipMain pip = optional.get();
            System.out.println("[" + ticker + "] 목표가 도달 → PIP 창 닫음");
            pip.stop();           // Stage 닫기 + 타임라인 정지
            pipWindows.remove(pip); // 리스트에서도 제거
            return true;
        } else {
            System.out.println("[" + ticker + "] PIP 창 없음");
            return false;
        }
    }

    // 존재 여부 확인
    public boolean exists(String ticker) {
        return pipWindows.stream().anyMatch(p -> p.getStockTicker().equals(ticker));
    }
}