package ac.dnd.server;

import org.springframework.boot.SpringApplication;

public class TestDndServerApplication {

    public static void main(String[] args) {
        SpringApplication.from(DndServerApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
