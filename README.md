# ReBalancedCaps
Invaders Always Capture &amp; Capture Time scaled to Enemy Claim Size

Minimum seconds = 35
Maximum seconds = 150
Enemy chunks = n

# Equation to determine cap times

Seconds = (0.6 * n) + Minimum Seconds

Seconds will always round up to the nearest 5 and max out at Maximum Seconds.

# Examples

You're attacking a 1 chunk claim. Your caps will go off after 40 seconds.

You're attacking a 20 chunk claim. Your caps will go off after 50 seconds.

You're attacking a 100 chunk claim. Your caps will go off after 95 seconds.

You're attacking a 150 chunk claim. Your caps will go off after 125 seconds.

You're attacking a 300 chunk claim. Your caps will go off after 150 seconds.
