local Debris = game:GetService("Debris")
local fxfunctions = {}

local CoreSystem = game.ReplicatedStorage:WaitForChild('CoreSystem');
local GameFunctions = require(CoreSystem:WaitForChild('Modules').ModelControllers.GameFunctions);

--Creates a "Splatter" effect of count parts centered around projectile using provided model as object being splattered
function fxfunctions.ModelSplatter(projectile, model, count)
	projectile.Transparency = 1;
	for i = 1, count do
		local s = model:Clone();
		local v = Vector3.new(math.random(-2,2), math.random(0,4), math.random(-2,2));
		s.Handle.Velocity = 15 * v;
		s.Handle.CFrame = CFrame.new(projectile.Position + v, v);
		s.Parent = workspace.FX;
		game:GetService("Debris"):AddItem(s, 0.5);
	end
    return true
end

--Creates a part "Splatter" effect using provided information to create splatter part
function fxfunctions.Splatter(projectile, count, material, size, shape, color)
	projectile.Transparency = 1;
	for i = 1, count do
		local s = Instance.new("Part");
		s.Size = size;
		s.Shape = shape;
		s.Color = color;
		s.Material = material;
		local v = Vector3.new(math.random(-2,2), math.random(0,6), math.random(-2,2));
		s.Velocity = 10 * v;
		s.CFrame = CFrame.new(projectile.Position + v, v);
		s.Parent = workspace.FX;
		game:GetService("Debris"):AddItem(s, .75);
	end
	return true;
end

--Creates an outline around the part.
function fxfunctions.HighlightPart(projectile, hit, color, time)
	if hit then
		local highlight = Instance.new('SelectionBox');
		highlight.Adornee = hit;
		highlight.Color3 = color;
		highlight.Parent = hit;
        if (time > 0) then
            Debris:AddItem(highlight, time)
        end
	else
		return("No hit part detected.")
	end
end

--Creates an explosion effect around the projectile
--Fidelity creates either an explosion of multi-color parts or a spherical explosion
function fxfunctions.Explosion(projectile, size, fidelity, colors)
	projectile.Transparency = 1;
	--Higher quality explosions
	if fidelity == 1 or projectile.Name == 'RightHand' then
		for i = 1, size do
			local s = Instance.new("Part");
			s.Size = Vector3.new(.5,.5,.5);
			s.Color = colors[math.random(1,#colors)];
			s.Material = Enum.Material.Neon;
			local v = Vector3.new(math.random(-4,4), math.random(0,6), math.random(-4,4));
			s.Velocity = 15 * v;
			s.CFrame = CFrame.new(projectile.Position + v, v);
			s.Parent = workspace.FX;
			game:GetService("Debris"):AddItem(s, .75);
		end
		return true
	elseif fidelity == 0 then --Low quality explosions
		local explosion = CoreSystem.Objects.GameObjects.Explosion:Clone();
		explosion.Inner.Color = colors[1];
		explosion.Outer.Color = colors[2];
		
		explosion:SetPrimaryPartCFrame(projectile.CFrame);
		explosion.Parent = workspace;
		game:GetService("Debris"):AddItem(explosion, .25 * (size / 8));
		local inner = explosion.Inner;
		local outer = explosion.Outer;
		for i = 0.25, size do
			GameFunctions.Wait();
			inner.Size = Vector3.new(i, i, i);
			outer.Size = Vector3.new(i + .25, i+ .25, i+ .25);
		end
		GameFunctions.Wait()
		for i = size - .75, .25, -1 do
			GameFunctions.Wait();
			inner.Size = Vector3.new(i, i, i);
			outer.Size = Vector3.new(i + .25, i+ .25, i+ .25);
		end
			
		return true
	end
	return false;
end

return fxfunctions
