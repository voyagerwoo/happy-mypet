package vw.app.happymypet.infras.metrics;

import org.springframework.data.util.Pair;

import java.util.List;

public interface MetricsMetadata {
    List<Pair<String, String>> dimensions();
}
