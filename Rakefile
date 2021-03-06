VERSION = "0.0.1"

def colorize(text, color)
  color_codes = {
    :black    => 30,
    :red      => 31,
    :green    => 32,
    :yellow   => 33,
    :blue     => 34,
    :magenta  => 35,
    :cyan     => 36,
    :white    => 37
  }
  code = color_codes[color]
  if code == nil
    text
  else
    "\033[#{code}m#{text}\033[0m"
  end
end

def publish(username, repo, path, file, content_type, data_type="--data")
  sh "curl -u #{username} #{repo}#{path} --request PUT -H 'Content-Type:#{content_type}' #{data_type} @#{file}"
end

def publish_gat(repo)
  username = "gat"
  path = "/com/gatournament/gat-java/0.0.1/gat-java.pom"
  file = "gat-java.pom"
  publish(username, repo, path, file, "text/xml")

  path = "/com/gatournament/gat-java/0.0.1/gat-java.jar"
  file = "dist/gat-java.jar"
  publish(username, repo, path, file, "application/octet-stream", "--data-binary")

  path = "/com/gatournament/gat-java/0.0.1/gat-java-sources.jar"
  file = "dist/gat-java-sources.jar"
  publish(username, repo, path, file, "application/octet-stream", "--data-binary")

  path = "/com/gatournament/gat-java/0.0.1/gat-java-javadoc.jar"
  file = "dist/gat-java-javadoc.jar"
  publish(username, repo, path, file, "application/octet-stream", "--data-binary")
end

task :tag => [:tests] do
  sh "git tag #{VERSION}"
  sh "git push origin #{VERSION}"
end

task :reset_tag => [] do
  sh "git tag -d #{VERSION}"
  sh "git push origin :refs/tags/#{VERSION}"
end

task :clean => [] do
  sh "ant clean"
end

task :tests => [] do
end

task :package => [:tests] do
  sh "ant jar"
end

task :install => [:package] do
  sh "ant publish-local"
end

task :publish_snapshot => [:package] do
  publish_gat("https://oss.sonatype.org/content/repositories/snapshots")
end

task :publish => [:package] do
  publish_gat("https://oss.sonatype.org/service/local/staging/deploy/maven2")
  # https://oss.sonatype.org/index.html#stagingRepositories
  # Then release manually in to send it to the central repository.
  # Steps to release it:
  # 1) close the open staging repository
  # 2) wait for the validation
  # 3) select the closed repository and set release
end

task :default => [:tests]
