package org.softwire.training.bookish.models.page;

import org.softwire.training.bookish.models.database.Technology;

import java.util.List;

public class SearchPageModel {
    private List<Technology> technologies;

    public List<Technology> getTechnologies() {
        return technologies;
    }
    public void setTechnologies(List<Technology> technologies) {
        this.technologies = technologies;
    }
}
