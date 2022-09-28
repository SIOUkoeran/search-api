package com.example.searchapi;

import com.example.searchapi.container.TestContainer;
import org.springframework.context.annotation.Import;

@Import({TestContainer.class})
public class BaseTest {
}
