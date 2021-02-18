package com.prosa.rivertech.rest.bankservices.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
class GeneratorAccountsNumberManagerTest {

    private GeneratorAccountsNumberManager generator;

    @BeforeEach
    public void before() {
        generator = new GeneratorAccountsNumberManager();
    }

    @Test
    void generateNumber_SameSizeAndOnlyDigits() {
        IntStream.range(0, 100).forEach(i -> {
            String number = generator.generateNumber();
            assertThat(number).hasSize(16).containsOnlyDigits();
        });
    }

    @Test
    void generateNumber_NoCollisions() {
        List<String> accountNumbers = new ArrayList<String>();
        IntStream.range(0, 100).forEach(i -> {
            accountNumbers.add(generator.generateNumber());
            try {
                // The current generator is not 100% collision free.
                // Collisions may happen if two account numbers are generated at the same time.
                // For this reason the generator is 3-random-digits + timestamp (3 random digits to avoid this collision)

                // The first implementation was 100% collision. It was applied as trigger function in PostgreSQL database, but H2 doesn't support this.
                // I created a simple method as alternative. It possible to improve, of course.
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        List duplicates =
                accountNumbers.stream().collect(Collectors.groupingBy(Function.identity()))
                        .entrySet()
                        .stream()
                        .filter(e -> e.getValue().size() > 1)
                        .collect(Collectors.toList());

        assertThat(duplicates).isEmpty();
    }


}