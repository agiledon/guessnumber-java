## Step 0：分解任务

分解任务：
* 随机生成答案
* 判断每次猜测的结果
* 检查输入是否合法
* 记录并显示历史猜测数据
* 判断游戏结果。判断猜测次数，如果满6次但是未猜对则判负；如果在6次内猜测的4个数字值与位置都正确，则判胜 

## Step 1: 选择第一个任务

随机生成答案和检查输入是否合法，存在一个共同任务：检查答案的合法性

因此，先增加一个任务：检查答案的合法性。由此开始编写单元测试。

在创建测试时，先确定项目中包的结构。由于当前任务牵涉到了一个模型对象`Answer`，因此可以考虑定义一个包`Model`。同时，驱动出`Answer`类。

测试场景：
* 数字只能在0~9之间
* 数字不能重复

在编写测试时，站在调用者角度，就要思考该如何创建`Answer`对象，才能让调用者觉得非常舒服。在设计时，需要一直遵循Kent Beck的“简单设计”原则。

例如，需求要求答案是4个数字，那到底该定义为四个固定的参数，还是使用变参？

注意，在设计模型对象时，尽量不要考虑UI的输入。例如这的接口就应该设计为int类型。

抛出异常时，也需要考虑异常类的位置。

TDD的过程中，要记得随时重构。


## Step 2：选择第二个任务

分解任务：
* 随机生成答案
* 判断每次猜测的结果
* 检查输入是否合法
* ~~检查答案的合法性~~
* 记录并显示历史猜测数据
* 判断游戏结果。判断猜测次数，如果满6次但是未猜对则判负；如果在6次内猜测的4个数字值与位置都正确，则判胜 

在已经提供了Answer类之后，可以考虑选择“随机生成答案”这一任务。该任务需要具备生成一个满足条件的随机数。由于生成随机数是一个不可预知的功能，我们考虑用Moq。

采用测试驱动开发时，我们并没有先去实现代码，而是编写测试用例：

```cs
        [Fact]
        public void Should_generate_actual_answer()
        {
            // given
            var mockRandom = new Mock<IRandomIntNumber>();
            mockRandom.SetupSequence(r => r.Next())
                .Returns(1)
                .Returns(2)
                .Returns(13)
                .Returns(3)
                .Returns(2)
                .Returns(4);

            var generator = new AnswerGenerator(mockRandom.Object);
            
            // when
            var answer = generator.Generate();

            // then
            Assert.Equal(new List<int> {1, 2, 3, 4}, answer.Numbers);
        }
```


注意，在使用Moq来模拟一个方法被多次调用时，要使用`SetupSequence()`方法。

由于使用了Mock，使得我可以控制IRandomIntNumber的返回值。上面的测试用例覆盖了生成越界数字、重复数字的情况。使得`AnswerGenerator`生成的答案一定是合法的。

在实现代码时，我发现需要将`IList<int>`作为参数创建一个`Answer`会变得更加简单。故而，我修改了`Answer`类的实现，增加了一个新的构造函数。这时，我需要增加新的单元测试。由于这个新的构造函数重用了之前的`Of()`方法，我只需要对新逻辑覆盖测试即可。增加的测试为：

```cs
        [Fact]
        public void Should_throw_exeption_if_input_list_not_equal_to_4()
        {
            Assert.Throws<InvalidCountException>(() => Answer.Of(new List<int> {1, 2, 3, 4, 5}));
        }
```

在有测试帮助下，重构也变得更加有信心。

例如之前的实现代码为：

```cs
        public Answer Generate()
        {
            IList<int> numbers = new List<int>(4);
            var number = _random.Next();
            numbers.Add(number);

            for (int i = 0; i < 3; i++)
            {
                int nextNumber;
                do
                {
                    nextNumber = _random.Next();
                } while (numbers.Contains(nextNumber) || NotInRange(nextNumber));
                numbers.Add(nextNumber);
            }
            return Answer.Of(numbers);
        }
```

后来我提取了`for`循环内的代码，定义为一个生成正确的无重复数字的方法。提取了方法后，发现第4、5行的代码没有必要单独处理。根据提取出来的方法判断，其实生成多个数字的逻辑是完全一样的：

```cs
       public Answer Generate()
        {
            IList<int> numbers = new List<int>(AnswerSize);

            for (var i = 0; i < AnswerSize; i++)
            {
                numbers.Add(GenerateUniqueCorrectNumber(numbers));
            }

            return Answer.Of(numbers);
        }

        private int GenerateUniqueCorrectNumber(IList<int> numbers)
        {
            int nextNumber;
            do
            {
                nextNumber = _random.Next();
            } while (numbers.Contains(nextNumber) || NotInRange(nextNumber));

            return nextNumber;
        }
```

这个重构做了部分手动工作，将原来的第4、5行的代码去掉了，然后将遍历的次数改为4。由于有测试保证，重构之后再运行一次测试，测试通过，就说明重构没有引入问题。


当我们为`Answer`引入了新的工厂方法之后，我们发现为`Answer`的有效性验证定义了太多细粒度的异常。其实这些异常的差别仅在于异常信息的不同。故而都可以定义为`InvalidAnswerException`。

在测试方法中，需要验证异常时，就不仅要验证异常的类型，还要验证异常的消息。xUnit提供了`Record.Exception(lambda)`方法，来记录异常。然后对返回的异常进行断言。这种方式其实更符合Given-When-Then或AAA Pattern。

```cs
            var exception = Record.Exception(() => Answer.Of(-1, 1, 2, 9));

            Assert.IsType<InvalidAnswerException>(exception);
            Assert.Equal("The number must be between 0 to 9.", exception.Message);
```

## Step 3: 选择第三个任务

分解任务：
* ~~随机生成答案~~
* 判断每次猜测的结果
* 检查输入是否合法
* ~~检查答案的合法性~~
* 记录并显示历史猜测数据
* 判断游戏结果。判断猜测次数，如果满6次但是未猜对则判负；如果在6次内猜测的4个数字值与位置都正确，则判胜 

从依赖顺序看，“判断每次猜测的结果”与“检查输入是否合法”没有依赖关系，后面两个任务与第二个任务存在依赖。考虑“检查输入是否合法”任务并非模型的核心，所以选择的第三个任务为：判断每次猜测的结果。

首先思考该任务的职责承担者。站在测试调用者的角度看，是谁负责进行每次猜测呢？以下是一些候选：
* Game
* Player

在面向对象设计中，我们要习惯于每个对象其实都有“智能意识”，而不仅限于代表人的对象角色。例如`Driver`确实具有开车的能力，但`Car`也有智能的自我运行的能力。这个和现实世界的对象是不同的。因此，游戏`Game`虽然是Player来玩的，但`Game`自身也应当有足够的智能。

仔细分析当前任务，任务描述的“每次猜测”，站在游戏的角度来看，应该是指游戏的每一局。`Game`可以猜测多次，而每次就是一局`Round`。因此当前任务的模型对象应该是`Round`。故而应该定义`RoundTest`。

编写测试用例是比较容易的。需求已经给出了多种情况。由于输入不正确的情况已经在`Answer`中做了验证，剩下的就只需要考虑是否猜对的情况。

编写测试时，要充分利用测试框架的特性。由于我们使用了xUnit，测试用例无非是多种数据组合，反应多种结果的不同。因此可以使用`[Theory]`来编写测试。因此，测试方法就应该为：

```cs
        [Theory]
        [MemberData(nameof(GuessRounds))]
        public void Should_guess_numbers(Answer answer, string guessResult)
        {

        }

        public static IEnumerable<object[]> GuessRounds()
        {
            yield return new object[] {Answer.Of(1, 5, 6, 7), "1A0B"};
        }
```

接下来就可以从调用者角度驱动出`Round`的接口：

```cs
        [Theory]
        [MemberData(nameof(GuessRounds))]
        public void Should_guess_numbers(Answer answer, string expectedResult)
        {
            var actualAnswer = Answer.Of(1, 2, 3, 4);
            var round = new Round(actualAnswer);

            var actualResult = round.Guess(answer);

            Assert.Equal(expectedResult, actualResult);
        }
```

注意，游戏实际的答案应该作为构造函数参数传入给`Round`，而每次猜测的答案则通过方法参数传入。注意两者的区别。

在实现`Guess()`方法时，我们希望比较输入的答案与实际的答案是否匹配。这个逻辑应该放在哪儿？

显然，根据信息专家模式，只有Answer才具备比较的知识，应该将匹配的逻辑分配给`Answer`。

在实现了`Answer`的`Guess()`方法后，要及时对代码进行重构：

```cs
        public string Matches(Answer inputAnswer)
        {
            var allRight = 0;
            var valueRight = 0;
            foreach (var actualNumber in Numbers)
            {
                var outerIndex = Numbers.IndexOf(actualNumber);

                foreach (var inputNumber in inputAnswer.Numbers)
                {
                    var innerIndex = inputAnswer.Numbers.IndexOf(inputNumber);
                    if (inputNumber == actualNumber && outerIndex == innerIndex)
                    {
                        allRight++;
                        continue;
                    }

                    if (inputNumber == actualNumber)
                    {
                        valueRight++;
                    }
                }
            }

            return $"{allRight}A{valueRight}B";
        }
    }
```

虽然不是太糟糕，太对比位置和值的逻辑还是不太清楚。重构后：

```cs
        public string Matches(Answer inputAnswer)
        {
            var allRight = 0;
            var valueRight = 0;
            foreach (var actualNumber in Numbers)
            {
                foreach (var inputNumber in inputAnswer.Numbers)
                {
                    if (SameValue(inputNumber, actualNumber) && SamePosition(inputAnswer, actualNumber, inputNumber))
                    {
                        allRight++;
                        continue;
                    }

                    if (SameValue(inputNumber, actualNumber))
                    {
                        valueRight++;
                    }
                }
            }

            return $"{allRight}A{valueRight}B";
        }
```

虽然`SameValue()`方法的代码很简单，但提取出来，一方面确保二者的抽象层次，另一方面也是希望体现游戏中要求相同值和相同位置的语义。

对于for循环内部的每个分支，就无需再提取方法了。过犹不及，表达直观的意思就足够了。

注意：每次重构之前和之后都要确保测试是通过的。

虽然是在`Answer`类中定义了`Matches()`方法，但我们不必要给它增加测试，因为在`Round`的测试中已经覆盖了`Matches()`方法。

## Step 4: 选择第四个任务

分解任务：
* ~~随机生成答案~~
* ~~判断每次猜测的结果~~
* 检查输入是否合法
* ~~检查答案的合法性~~
* 记录并显示历史猜测数据
* 判断游戏结果。判断猜测次数，如果满6次但是未猜对则判负；如果在6次内猜测的4个数字值与位置都正确，则判胜

相较于任务“判断游戏结果”，“记录并显示历史猜测数据”任务只是一个辅助功能，所以我考虑先完成任务“判断游戏结果”。

既然之前用`Round`来表示每一局，现在就应该用`Game`来表示游戏。

测试用例比较简单，因为每一局的猜测结果已经在`RoundTest`覆盖，所以现在只需要考虑两种情形：
* 在规定的6次内都未猜对
* 在规定的6次内猜对

在编写测试`Should_report_lose_result_if_all_6_times_are_wrong()`时，我需要创建一个`Game`对象。站在测试调用者的角度，我开始思考规定的局数是设置在`Game`内部，还是作为构造函数参数。最终决定放在构造函数参数，以便于保证它的可调整性。（结果，在编写测试时，发现这一参数带来的好处是，可以设置为更少的局数，方便编写测试）

接下来还要思考`Game`定义的接口是什么呢？方法名有两个选择：
* Guess
* Play

站在游戏的角度来讲，命名为`Guess`可能更符合业务场景的含义。`Play()`更适合分配给`Player`。

次数的控制是在`Game`内部履行的，在写测试的时候，需要模拟玩家猜测6次。传入的参数自然是`Answer`，但是返回的结果呢？如果是`0A0B`这样的提示信息，当游戏失败或成功结束时，显示的信息就不再是`0A0B`这样的猜测结果了。虽然都可以定义为string类型，但我认为这是两种不同的结果，只是简单地定义为string，是不合理的。故而需要封装为一个`GuessResult`对象。如果最终成功或失败了，需求并没有说明该显示什么，那就先定义为枚举吧。

## Step 5: 选择第五个任务

分解任务：
* ~~随机生成答案~~
* ~~判断每次猜测的结果~~
* 检查输入是否合法
* ~~检查答案的合法性~~
* 记录并显示历史猜测数据
* ~~判断游戏结果。判断猜测次数，如果满6次但是未猜对则判负；如果在6次内猜测的4个数字值与位置都正确，则判胜~~

现在选择任务“记录并显示历史猜测数据”。由于之前在思考游戏结果时，已经封装了`GuessResult`。因此要完成第5个任务，就只需要加上对历史猜测结果的记录即可。

由于测试用例是一样的，只是要多增加验证的结果。因此只需要修改之前的测试用例即可。

**注意：** 需求要求记录的猜测历史记录包括：
* 之前猜测的数字
* 之前猜测的结果

要表达这一概念，就必须定义类来封装。于是引入了`Guess`概念。另外，注意需求要求历史记录仅记录之前的猜测信息，并不包含本次猜测的结果。

对`GuessResult`的验证存在重复代码，可以对测试进行重构。

---

`GuessResult`这个领域模型的名称不能更好地体现为游戏的结果，相反，更像是每局猜测的结果。因此将其更名为`GameResult`。同时，`GameStatus`的`TBD`值也不符合业务含义，将其更名为`Continue`：

```cs
namespace GuessNumber.Model
{
    public class GameResult
    {
        public string GuessResult { get; set; }
        public GameStatus Status { get; set; }
        public IList<Guess> GuessHistory { get; } = new List<Guess>();
        
        public void AddGuessHistory(Guess guess)
        {
            GuessHistory.Add(guess);
        }
    }
}

namespace GuessNumber.Model
{
    public enum GameStatus
    {
        Continue, Lose, Win
    }
}
```

`Round`的`guess()`方法返回的才是guess result，只是没有必要定义对象罢了。
