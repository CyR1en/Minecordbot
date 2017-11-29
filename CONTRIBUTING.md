# How to contribute
Note: Addapted from the Puppet Contributing Guidelines

Minecordbot is an free and openly sourced project under the GNU GPLv3 or later version (see [LICENSE](https://github.com/CyR1en/Minecordbot/blob/master/LICENSE))
As such, we also welcome contributions from the community. Not all Pull Requests
may be acceptable for inclusion (though you're free to use them in your own forks) due
to various reason, such as vulnerabilities, build issues, or poor legibility. The cleaner your
code, the more likely it will be included.

## Getting Started

* Make sure you have a [Discord account](https://discordapp.com/register).
  * What good is a Discord bot without Discord?
* Make sure you have a [GitHub account](https://github.com/signup/free).
* Submit a ticket for your issue, assuming one does not already exist.
  * Clearly describe the issue including steps to reproduce when it is a bug.
  * Make sure you fill in the earliest version that you know has the issue.
* Fork the repository on GitHub.
* Create or use a per-existing BukkitAPI server for testing your patches
  * Keep in mind the target environment (Java 8 and the latest build of Spigot)
  
## Making Changes

* Create a topic branch from where you want to base your work.
  * This is usually the Development branch.
  * Only target the Release branch if you are certain your fix must be on that
    branch. (ie: a hot-fix for a security vulnerability)
  * To quickly create a topic branch based on Development, run `git checkout -b
    fix/development/my_contribution development`. Please avoid working directly on the
    `development` branch.
  * **Never work directly on Master**

* Make commits of logical units.
* Check for unnecessary white-space with `git diff --check` before committing.
* Make sure your commit messages are in the proper format. If the commit
  addresses an issue filed in the
  [Minecordbot Issue Tracker](https://github.com/CyR1en/Minecordbot/issues), start
  the first line of the commit with the issue number in parentheses, following a hashtag.
  
  ```
      (MCB #45) Implement Conversion of Discord Emoji to compatible Smilies (ASCII)

      This patch is meant to fix a relay issue when using common, auto-converted
      emoji from Discord. Without this patch, certain messages may relay blank
      sections. For example, `:)` would be translated by Discord into `:smile:`
      before the message is read by Minecordbot. This patch allows Minecordbot to
      recognize common emoji and translate them into ASCII compatible smilies on
      relay to aleviate this problem.
      
      The first line is a real-life imperative statement with a ticket number
      from our issue tracker. The body describes the behavior without the patch,
      why this is a problem, and how the patch fixes the problem when applied.
  ```
* Make sure you have added the necessary tests for your changes.
* Run our standard tests to ensure regressions do not occure.

## Writing Translatable Code

We use [localization files](https://github.com/CyR1en/Minecordbot/tree/master/src/main/resources/localizations) to
display text not provided by Minecraft itself, with English being the basis.
When adding user-facing strings to your work, follow these guidelines:
* Use full sentences. Strings built up out of concatenated bits are harder to translate.
* Include your strings in **all** of the localization files
* Include a notice in your Pull Request that the translation of your strings will be necessary.

It is the responsibility of contributors and code reviewers to ensure that all
user-facing strings are marked in new PRs and that translators have been notified before merging.

## Making Trivial Changes

For commits that address trivial repository maintenance tasks or packaging
issues, start the first line of the commit with `(maint)` or `(packaging)`,
respectively. Use (doc) when making changes to any of our markdown files, or when
adding a reference license to a third party work being made use of.
You typically do not need to submit an Issue for these changes.

## Submitting Changes

* Push your changes to a topic branch in your fork of the repository.
* Submit a pull request to the upstream repository (CyR1en/Minecordbot).
* Reference any addressed issues in the body of your pull request.
  * See [this GitHub Help article](https://help.github.com/articles/closing-issues-using-keywords/) for more information.
* After feedback has been given we expect responses within two weeks. After two
  weeks we may close the pull request if it isn't showing any activity.

## Revert Policy
[INSERT REVERT POLICY]

### Summary

* Changes resulting in test pipeline failures will be reverted if they cannot
  be resolved within a couple of days.

## Additional Resources

* [Minecordbot Code of Conduct](https://github.com/CyR1en/Minecordbot/blob/master/CODE_OF_CONDUCT.md)
* [Bug tracker (GitHub)](https://github.com/CyR1en/Minecordbot/issues)
* [General GitHub documentation](https://help.github.com/)
* [GitHub pull request documentation](https://help.github.com/articles/creating-a-pull-request/)
* Minecordbot's Official Discord ([C Y R I E N](https://discord.gg/rEK5XmV))
