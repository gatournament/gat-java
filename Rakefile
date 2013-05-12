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

task :publish => [:package, :tag] do
  sh "ant publish"
end

task :default => [:tests]
