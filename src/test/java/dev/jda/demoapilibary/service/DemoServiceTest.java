package dev.jda.demoapilibary.service;

import dev.jda.demoapilibary.entity.Demo;
import dev.jda.demoapilibary.repository.DemoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
//@Import(ModelMapperConfiguration.class)
class DemoServiceTest {

    public static final String DEMO_CODE = "1234";

    @Mock
    protected DemoRepository demoRepository;

    private DemoService unitToTest;

    @BeforeEach
    public void beforeEachTest(){
        this.unitToTest = new DemoService(demoRepository);
    }

    @Test
    void getDemoInfo(){
        when(demoRepository.findByCode(any())).thenReturn(Optional.of(createDemo()));
        Demo result = unitToTest.getDemoByCode(DEMO_CODE);
        assertEquals(createDemo(), result);
    }

    @Test
    void getDemoNotFound(){
        when(demoRepository.findByCode(any())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> unitToTest.getDemoByCode(DEMO_CODE));
    }

    private Demo createDemo(){
        return Demo.builder()
                .code("1234")
                .name("henk")
                .uuid("f3as5jj-8819-9952-b3ds-l0os8iwwejsa")
                .build();
    }
}
