package dev.thomasglasser.tommylib.api.data.modonomicons;

import com.klikli_dev.modonomicon.api.ModonomiconConstants;
import com.klikli_dev.modonomicon.api.datagen.BookProvider;
import com.klikli_dev.modonomicon.api.datagen.CategoryProvider;
import com.klikli_dev.modonomicon.api.datagen.EntryProvider;
import com.klikli_dev.modonomicon.api.datagen.ModonomiconLanguageProvider;
import com.klikli_dev.modonomicon.api.datagen.book.BookCategoryModel;
import com.klikli_dev.modonomicon.api.datagen.book.BookEntryModel;
import com.klikli_dev.modonomicon.api.datagen.book.BookModel;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public abstract class AdditionalEntryProvider implements DataProvider
{
	protected PackOutput packOutput;
	protected String modId;
	protected ModonomiconLanguageProvider lang;
	protected CategoryProvider categoryProvider;
	private final Map<ResourceLocation, BookEntryModel> entries = new HashMap<>();

	public AdditionalEntryProvider(PackOutput packOutput, String modId, ModonomiconLanguageProvider lang, String existingBookModId, String existingBookId, String existingCategoryId, String[] existingEntryMap)
	{
		this.packOutput = packOutput;
		this.modId = modId;
		this.lang = lang;
		var bookProvider = new BookProvider(existingBookId, null, existingBookModId, lang) {
			@Override
			protected void registerDefaultMacros() {
			}

			@Override
			protected BookModel generateBook() {
				return BookModel.create(modLoc(context.book()), context.bookName())
						.withCategories(
								categoryProvider.generate()
						);
			}

			@Override
			public void generate()
			{
				super.generate();
			}
		};
		this.categoryProvider = new CategoryProvider(bookProvider, existingCategoryId) {
			@Override
			protected String[] generateEntryMap() {
				return existingEntryMap;
			}

			@Override
			protected void generateEntries() {
			}

			@Override
			protected BookCategoryModel generateCategory() {
				return BookCategoryModel.create(new ResourceLocation(existingBookModId, existingCategoryId), "");
			}
		};
		bookProvider.generate();
	}

	protected abstract void generate();

	@Override
	public @NotNull CompletableFuture<?> run(@NotNull CachedOutput cache) {

		List<CompletableFuture<?>> futures = new ArrayList<>();

		Path dataFolder = this.packOutput.getOutputFolder(PackOutput.Target.DATA_PACK);

		this.generate();

		for (BookEntryModel bookEntryModel : this.entries.values())
		{
			Path bookEntryPath = getPath(dataFolder, bookEntryModel);
			futures.add(DataProvider.saveStable(cache, bookEntryModel.toJson(), bookEntryPath));
		}


		return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
	}

	private Path getPath(Path dataFolder, BookEntryModel bookEntryModel) {
		ResourceLocation id = bookEntryModel.getId();
		return dataFolder
				.resolve(id.getNamespace())
				.resolve(ModonomiconConstants.Data.MODONOMICON_DATA_PATH)
				.resolve(bookEntryModel.getCategory().getBook().getId().getPath())
				.resolve("entries")
				.resolve(id.getPath() + ".json");
	}

	protected BookEntryModel add(Function<CategoryProvider, EntryProvider> entryProvider, char location)
	{
		BookEntryModel entry = entryProvider.apply(this.categoryProvider).generate(location);
		if (this.entries.containsKey(entry.getId()))
			throw new IllegalStateException("Duplicate book " + entry.getId());
		this.entries.put(entry.getId(), entry);
		return entry;
	}

	@Override
	public @NotNull String getName()
	{
		return "Additional Entries: " + modId;
	}
}
