package com.hacker.boot.elasticsearch;

import com.hacker.boot.elasticsearch.bean.Article;
import com.hacker.boot.elasticsearch.bean.Book;
import com.hacker.boot.elasticsearch.repository.BookRepository;
import io.searchbox.client.JestClient;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringbootDataElasticsearchApplicationTests {

	@Autowired
	private JestClient jestClient;

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private ElasticsearchTemplate elasticsearchTemplate;


	@Test
	public void testElasticSearchTemplate(){
		SearchQuery searchQuery = new NativeSearchQueryBuilder()
				.withQuery(new MatchAllQueryBuilder())
				.withFilter(new BoolQueryBuilder().must(new TermQueryBuilder("id", "1")))
				.build();

		Page<Book> sampleEntities =
				elasticsearchTemplate.queryForPage(searchQuery,Book.class);

		System.out.println(sampleEntities.get().map(Book::getBookName).collect(Collectors.toList()));
	}

	@Test
	public void test2(){
		SearchQuery searchQuery = new NativeSearchQueryBuilder()
				.withQuery(new MatchAllQueryBuilder())
				.withIndices("hacker")
				.withTypes("book")
				.withPageable(PageRequest.of(0, 1))
				.build();

		AggregatedPage<Book> books = elasticsearchTemplate.queryForPage(searchQuery, Book.class);

		System.out.println(books.get().map(Book::getBookName).collect(Collectors.toList()));
	}

	@Test
	public void testRepository(){
		Book book = new Book();
		book.setId(1);
		book.setBookName("西游记");
		book.setAuthor("吴承恩");

		bookRepository.index(book);
	}

	@Test
	public void testCustom(){
		List<Book> list = bookRepository.findByBookNameLike("游");
		System.out.println(list);
	}

	@Test
	public void contextLoads() {
		// 给 ES 索引（保存）一个文档
		Article article = new Article();
		article.setId(2);
		article.setAuthor("hacker");
		article.setTitle("好消息");
		article.setContent("hello world");

		//构建一个索引
		Index index = new Index.Builder(article).index("hacker_index").type("news").build();

		try {
			jestClient.execute(index);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	@Test
	public void search(){
		//搜索表达式
		String jsonSearch = "{\n" +
				"    \"query\" : {\n" +
				"        \"match\" : {\n" +
				"            \"content\" : \"hello\"\n" +
				"        }\n" +
				"    }\n" +
				"}";
		Search search = new Search.Builder(jsonSearch).addIndex("hacker_index").addType("news").build();

		try {
			SearchResult searchResult = jestClient.execute(search);
			System.out.println(searchResult.getJsonString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
