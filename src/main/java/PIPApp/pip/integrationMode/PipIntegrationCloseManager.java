package pip.integrationMode;

import config.manager.PreferencesManager;

import java.util.List;
import java.util.Optional;

public class PipIntegrationCloseManager {
    private final List<PipIntegrationMain> pipWindows;

    // 기존 PipMain 리스트를 주입받아 관리
    public PipIntegrationCloseManager(List<PipIntegrationMain> pipWindows) {
        this.pipWindows = pipWindows;
    }


    // 티커로 PIP 창 닫기
    public boolean closePipByTicker(String ticker) {
        Optional<PipIntegrationMain> optional = pipWindows.stream()
                .filter(p -> p.getStockTicker().equals(ticker))
                .findFirst();

        if (optional.isPresent()) {
            PipIntegrationMain pip = optional.get();
            System.out.println("[" + ticker + "] 목표가 도달 → PIP 창 닫음");
            pip.stop(1);           // Stage 닫기 + 타임라인 정지
            pipWindows.remove(pip); // 리스트에서도 제거
            new PreferencesManager().saveSettings();
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