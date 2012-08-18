package org.tessell.todomvc.client.app;

import static org.tessell.model.dsl.Binder.when;
import static org.tessell.model.dsl.TakesValues.textOf;
import static org.tessell.todomvc.client.views.AppViews.newStatsView;

import org.tessell.model.properties.StringProperty;
import org.tessell.model.values.DerivedValue;
import org.tessell.presenter.BasicPresenter;
import org.tessell.todomvc.client.model.AppState;
import org.tessell.todomvc.client.views.IsStatsView;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

public class StatsPresenter extends BasicPresenter<IsStatsView> {

  private final AppState state;

  public StatsPresenter(final AppState state) {
    super(newStatsView());
    this.state = state;
  }

  @Override
  public void onBind() {
    super.onBind();

    bind(state.numberLeft.asString()).to(textOf(view.numberLeft()));
    bind(leftText(state)).to(textOf(view.numberLeftWord()));
    bind(clearText(state)).to(textOf(view.clearCompletedAnchor()));
    when(state.doneTodos.size()).is(0).hide(view.clearCompletedAnchor());
    when(state.allTodos.size()).is(0).hide(view.stats());

    view.clearCompletedAnchor().addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        state.removeDone();
      }
    });
  }

  /** @return "item" or "items" based on number left */
  private static StringProperty leftText(final AppState state) {
    return new StringProperty(new DerivedValue<String>() {
      public String get() {
        return state.numberLeft.get() == 1 ? "item" : "items";
      }
    });
  }

  /** @return "Clear X completed item(s) */
  private static StringProperty clearText(final AppState state) {
    return new StringProperty(new DerivedValue<String>() {
      public String get() {
        int done = state.doneTodos.get().size();
        String itemText = done == 1 ? "item" : "items";
        return "Clear " + done + " completed " + itemText;
      }
    });
  }
}
