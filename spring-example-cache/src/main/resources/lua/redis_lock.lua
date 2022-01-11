local count = redis.call('get', KEYS[1])
local a = tonumber(count)
local b = tonumber(ARGV[1])
if a >= b then
    redis.call('set', KEYS[1], count-b)
    return 1
    end
return 0
