package com.example.searchapi.common.query;

import com.example.searchapi.address.model.Address;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.SuggestionBuilder;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.suggest.response.CompletionSuggestion;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Slf4j
public class QuerySuggestUtils {

    public List<String> convertToAddressList(CompletionSuggestion compSuggestion) {
        List<String> addressList = new LinkedList<>();
        StringBuilder sb = new StringBuilder();
        List<CompletionSuggestion.Entry> entryList = compSuggestion.getEntries();
        if(entryList != null) {
            for (CompletionSuggestion.Entry entry : entryList) {
                List<CompletionSuggestion.Entry.Option> options = entry.getOptions();
                if (options != null) {
                    for (CompletionSuggestion.Entry.Option option : options) {
                        SearchHit<Address> searchHit = option.getSearchHit();
                        Address address = searchHit.getContent();
                        sb.setLength(0);
                        sb.append(address.getAddress())
                                .append(" ")
                                .append(address.getPrimary_bun())
                                .append("-")
                                .append(address.getSecondary_bun())
                        ;
                        addressList.add(String.valueOf(sb));
                    }
                }
            }
        }
       return addressList;
    }

    public CompletionSuggestionBuilder createCompletionQuery(String s, int size) {
        return SuggestBuilders.completionSuggestion("address_suggest")
                .prefix(s, Fuzziness.AUTO)
                .size(size)
                ;
    }
    public SuggestBuilder createSuggestBuilder(List<String> queryName, SuggestionBuilder<?>... builder) {
        SuggestBuilder suggestBuilder = new SuggestBuilder();
        AtomicInteger atomicInteger = new AtomicInteger(0);
        Arrays.stream(builder)
                .forEach(b -> suggestBuilder.addSuggestion(queryName.get(atomicInteger.getAndIncrement()), b));

        return suggestBuilder;
    }
}
