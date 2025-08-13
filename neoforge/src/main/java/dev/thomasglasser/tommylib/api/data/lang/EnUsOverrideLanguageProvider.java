package dev.thomasglasser.tommylib.api.data.lang;

import net.minecraft.data.PackOutput;

public abstract class EnUsOverrideLanguageProvider extends ExtendedEnUsLanguageProvider {
    protected EnUsOverrideLanguageProvider(PackOutput output, String modId) {
        super(output, modId);
    }

    protected void addAdvancement(String category, String id, String suffix, String name) {
        add("advancement." + modId + "." + category + "." + id + "." + suffix, name);
    }

    protected void addCategoryName(String book, String category, String name) {
        addCategoryOverride(book, category, "name", name);
    }

    protected void addCategoryDescription(String book, String category, String name) {
        addCategoryOverride(book, category, "description", name);
    }

    protected void addEntryName(String book, String category, String entry, String name) {
        addEntryOverride(book, category, entry, "name", name);
    }

    protected void addEntryDescription(String book, String category, String entry, String name) {
        addEntryOverride(book, category, entry, "description", name);
    }

    protected void addPageTitle(String book, String category, String entry, String page, String name) {
        addPageOverride(book, category, entry, page, "title", name);
    }

    protected void addPageText(String book, String category, String entry, String page, String name) {
        addPageOverride(book, category, entry, page, "text", name);
    }

    protected void addCategoryOverride(String book, String category, String override, String name) {
        addOverride(book + "." + category + "." + override, name);
    }

    protected void addEntryOverride(String book, String category, String entry, String override, String name) {
        addOverride(book + "." + category + "." + entry + "." + override, name);
    }

    protected void addPageOverride(String book, String category, String entry, String page, String override, String name) {
        addOverride(book + "." + category + "." + entry + "." + page + "." + override, name);
    }

    protected void addOverride(String override, String name) {
        add("book." + modId + "." + override, name);
    }
}
