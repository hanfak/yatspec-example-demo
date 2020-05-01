package endtoendtests.othergivens;

import com.googlecode.yatspec.state.givenwhenthen.GivensBuilder;
import com.googlecode.yatspec.state.givenwhenthen.InterestingGivens;

import java.util.ArrayList;

import static java.util.Arrays.asList;

public class CompositeGivens implements GivensBuilder {


    private final ArrayList<GivensBuilder> givensBuilders;

    public CompositeGivens(GivensBuilder[] givensBuilders) {
        this.givensBuilders = new ArrayList<>(asList(givensBuilders));
    }

    public InterestingGivens build(InterestingGivens interestingGivens) throws Exception {
        for (GivensBuilder givensBuilder : givensBuilders) {
            givensBuilder.build(interestingGivens);
        }
        return interestingGivens;
    }

    public CompositeGivens and(GivensBuilder givensBuilder) {
        if (givensBuilder != null) {
            givensBuilders.add(givensBuilder);
        }
        return this;
    }

    public static CompositeGivens given(GivensBuilder... givensBuilders) {
        return new CompositeGivens(givensBuilders);
    }
}
