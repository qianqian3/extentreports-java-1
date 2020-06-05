## ExtentReports [![Join the chat at https://gitter.im/anshooarora/extentreports](https://badges.gitter.im/anshooarora/extentreports.svg)](https://gitter.im/anshooarora/extentreports?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge) [![Build Status](https://travis-ci.com/extent-framework/extentreports-java.svg?branch=v5.0.x)](https://travis-ci.com/extent-framework/extentreports-java) [![Codacy Badge](https://api.codacy.com/project/badge/Grade/dbdc8c04b0f84489a738f064f28a82fa)](https://www.codacy.com/app/anshooarora/extentreports?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=extent-framework/extentreports&amp;utm_campaign=Badge_Grade)

### Maven

```
<dependency>
  <groupId>com.aventstack</groupId>
  <artifactId>extentreports</artifactId>
  <version>5.0.0-SNAPSHOT</version>
</dependency>
```

### What's new

* You can now create your own custom logs, tables with custom headers, pass your POJOs directly
to be converted into a `<table>` etc. You can also specify any CSS classes to be applied on
the table, like in the below example with "table-sm" (a bootstrap table class).

```
public class MyCustomLog {
    private List<Object> names = Arrays.asList("Anshoo", "Extent", "Klov");
    private Object[] favStack = new Object[]{"Java", "C#", "Angular"};
    @MarkupIgnore
    private List<Object> ignored = Arrays.asList("Anshoo/Ignore", "Extent/Ignore", "Klov/Ignore");
    private Map<Object, Object> items = new HashMap<Object, Object>() {
        {
            put("Item1", "Value1");
            put("Item2", "Value2");
            put("Item3", "Value3");
        }
    };
}
extent.createTest("GeneratedLog").generateLog(Status.FAIL, MarkupHelper.toTable(new MyObject(), "table-sm"));
```

* MarkupHelper.createOrderedList(object) (see below)
* MarkupHelper.createUnorderedList(object) (see below)

```
String[] items = new String[] { "Item1", "Item2", "Item3" };
Set<Object> items = new HashSet<>(Arrays.asList("Item1", "Item2", "Item3"));
List<Object> items = Arrays.asList(new Object[] { "Item1", "Item2", "Item3" });
extent.createTest("Test").info(MarkupHelper.createOrderedList(items));
```

```
Map<Object, Object> items = new HashMap<Object, Object>()
{{
     put("Item1", "Value1");
     put("Item2", "Value2");
     put("Item3", "Value3");
}};
extent.createTest("Test").info(MarkupHelper.createUnorderedList(items).getMarkup());
```

* MarkupHelper.toTable(object)

```
public class MyObject {
    private List<Object> names = Arrays.asList("Anshoo", "Extent", "Klov");
    private Object[] favStack = new Object[]{"Java", "C#", "Angular"};
    @MarkupIgnore
    private List<Object> ignored = Arrays.asList("Anshoo/Ignore", "Extent/Ignore", "Klov/Ignore");
    private Map<Object, Object> items = new HashMap<Object, Object>() {
        {
            put("Item1", "Value1");
            put("Item2", "Value2");
            put("Item3", "Value3");
        }
    };
}
extent.createTest("Test").info(MarkupHelper.toTable(new MyObject()));
```

* Report filters for Status/Tag specific reports:

```java
ExtentReports extent = new ExtentReports();
// will only contain failures
ExtentSparkReporter sparkFail = new ExtentSparkReporter("spark/fail.html");
sparkFail.withFilter(ContextFilter.builder().status(Status.FAIL).build());
// will contain all tests
ExtentSparkReporter sparkAll = new ExtentSparkReporter("spark/all.html");
extent.attachReporter(sparkFail, sparkAll);
```

### Breaking changes

* ExtentReports::getStartedReporters removed
* ExtentReports::detachReporter removed
* ExtentReports:setTestRunnerOutput renamed to `addTestRunnerOutput`
* Status::ERROR, Status::FATAL, Status::DEBUG removed

### What's not working (yet)

* OfflineMode
* Append, CreateDomainFromJsonArchive
* Loading external configuration.xml
* You tell me..

### Upcoming

* See [v5.0.x milestones](https://github.com/extent-framework/extentreports-java/issues?q=is%3Aopen+is%3Aissue+milestone%3A5.0.x)
* Want to see a feature added? You can raise one [here](https://github.com/extent-framework/extentreports-java/issues?q=is%3Aopen+is%3Aissue+milestone%3A5.0.x)

### License

Apache-2.0
