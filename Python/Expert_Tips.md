
### Zen of Python

 - Beautiful is better than ugly.
 - Explicit is better than implicit.
 - Simple is better than complex.
 - Complex is better than complicated.
 - Flat is better than nested.
 - Sparse is better than dense.
 - Readability counts.
 - Special cases aren't special enough to break the rules.
 - Although practicality beats purity.
 - Errors should never pass silently.
 - Unless explicitly silenced.
 - In the face of ambiguity, refuse the temptation to guess.
 - There should be one-- and preferably only one --obvious way to do it.
 - Although that way may not be obvious at first unless you're Dutch.
 - Now is better than never.
 - Although never is often better than *right* now.
 - If the implementation is hard to explain, it's a bad idea.
 - If the implementation is easy to explain, it may be a good idea.
 - Namespaces are one honking great idea -- let's do more of those!
 
### Performance-Complexity Hacks

 - Import is better than from.
 - Iterations is better than Looping.(List traversal)
 - Map is better than Zip. (List Comprehension)
 - Coupling: use arguments for inputs and  return for outputs.
 - Use global variables only when truly necessary.
 - Don’t change mutable arguments unless the caller expects it.
 - Each function should have a single, unified purpose. Don't mess it. 
 - Avoid changing variables in another module file directly.
 - Lambda over zip are better than simple.(list(map((lambda x: x ** 2), range(10))))
 - [] Becomes Comprehension, () Becomes Generator. (List)
 - Avoid Nested Comprehenssion on List. 