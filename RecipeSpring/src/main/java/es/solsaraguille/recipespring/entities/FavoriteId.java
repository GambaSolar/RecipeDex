package es.solsaraguille.recipespring.entities;

import java.io.Serializable;
import java.util.Objects;

public class FavoriteId implements Serializable {

    private Integer user;
    private Integer recipe;

    public FavoriteId() {}

    public FavoriteId(Integer user, Integer recipe) {
        this.user = user;
        this.recipe = recipe;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FavoriteId)) return false;
        FavoriteId that = (FavoriteId) o;
        return Objects.equals(user, that.user) &&
                Objects.equals(recipe, that.recipe);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, recipe);
    }
}
